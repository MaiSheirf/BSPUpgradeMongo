    package com.mongo.BSPUpgrade.service;

    import com.mongo.BSPUpgrade.config.MultiMongoConfig;
    import com.mongo.BSPUpgrade.dto.MobileExceptionDTO;
    import com.mongo.BSPUpgrade.dto.MobileGlobalTRXDTO;
    import com.mongo.BSPUpgrade.entity.MongoEsbAudit;
    import com.mongo.BSPUpgrade.entity.MongoServiceInf;
    import lombok.extern.slf4j.Slf4j;
    import org.bson.Document;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.ComponentScan;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.data.domain.Sort;
    import org.springframework.data.mongodb.core.MongoTemplate;
    import org.springframework.data.mongodb.core.query.Criteria;
    import org.springframework.data.mongodb.core.query.Query;
    import org.springframework.stereotype.Service;

    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.time.*;
    import java.time.format.DateTimeFormatter;
    import java.time.format.DateTimeFormatterBuilder;
    import java.time.format.DateTimeParseException;
    import java.time.temporal.ChronoField;
    import java.time.temporal.ChronoUnit;
    import java.util.*;
    import java.util.regex.Pattern;
    import java.util.stream.Collectors;

    @Service
    @Slf4j
    @Configuration
    @ComponentScan
    public class ServiceRouting implements ServiceList {

        @Autowired
        private MultiMongoConfig multiMongoConfig;

        @Autowired
        private ServiceRoutingStore serviceRoutingStore;

    /*    @Override
        public List<String> getServiceNamesByStatus(String databaseName, String status) {
            MongoTemplate template = multiMongoConfig.getTemplate(databaseName);

            Query query = new Query();
            query.addCriteria(Criteria.where("status").is(status));

            List<MongoServiceInf> results = template.find(query, MongoServiceInf.class, "service_inf");

            return results.stream()
                    .map(MongoServiceInf::getServiceName)
                    .collect(Collectors.toList());
        }*/




        @Override
        public List<MobileGlobalTRXDTO> getMobileGlobalTRX(String databaseName, String tableName,
                                                        String fromDate, String toDate,
                                                        String mobileMSISDN, String mobileCustomerID, String mobileRequestID,
                                                        String mobileSubscriberID,
                                                        String eventKey1, String eventKey2, String eventKey3,
                                                        String eventKey4, String eventKey5) {


            if (tableName == null || tableName.trim().isEmpty()) {
                throw new IllegalArgumentException("Collection (table) name must not be null or empty");
            }

            MongoTemplate mongoTemplate = multiMongoConfig.getTemplate(databaseName);
            Query query = new Query();


            Instant to = parseFlexibleDate(toDate);
            if (to != null) {
                // Move to end of same day in Cairo timezone (23:59:59.999)
                ZonedDateTime endOfDay = to.atZone(ZoneId.of("Africa/Cairo"))
                        .withHour(23).withMinute(59).withSecond(59).withNano(999_000_000);
                to = endOfDay.toInstant(); // convert to UTC
            }

            Instant from = parseFlexibleDate(fromDate);
            if (from != null) {
                // Move to end of same day in Cairo timezone (23:59:59.999)
                ZonedDateTime endOfDay = from.atZone(ZoneId.of("Africa/Cairo"))
                        .withHour(23).withMinute(59).withSecond(59).withNano(999_000_000);
                from = endOfDay.toInstant(); // convert to UTC
            }

            if (mobileMSISDN != null) query.addCriteria(Criteria.where("MSISDN").is(mobileMSISDN));
            if (mobileCustomerID != null) query.addCriteria(Criteria.where("CUSTOMER_ID").is(mobileCustomerID));
            if (mobileRequestID != null) query.addCriteria(Criteria.where(" REQUEST_ID").is(mobileRequestID)); // space
            if (mobileSubscriberID != null) query.addCriteria(Criteria.where("SUBSCRIBER_ID").is(mobileSubscriberID));
            if (eventKey1 != null) query.addCriteria(Criteria.where("BUSINESS_KEY1").is(eventKey1));
            if (eventKey2 != null) query.addCriteria(Criteria.where("BUSINESS_KEY2").is(eventKey2));
            if (eventKey3 != null) query.addCriteria(Criteria.where("BUSINESS_KEY3").is(eventKey3));
            if (eventKey4 != null) query.addCriteria(Criteria.where("BUSINESS_KEY4").is(eventKey4));
            if (eventKey5 != null) query.addCriteria(Criteria.where("BUSINESS_KEY5").is(eventKey5));

            query.with(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "IN_TIMESTAMP"));
            System.out.println("Final Mongo Query: " + query);
            // ❌ Cannot map directly to DTO due to fields with space
            List<Document> docs = mongoTemplate.find(query, Document.class, tableName.trim());

            List<MobileGlobalTRXDTO> result = new ArrayList<>();
            for (Document doc : docs) {
                MobileGlobalTRXDTO dto = new MobileGlobalTRXDTO();
                dto.setRequestId(doc.getString(" REQUEST_ID")); // preserve space
                dto.setMsgId(doc.getString("MSG_ID"));
                dto.setFlowName(doc.getString(" FLOW_NAME"));  // preserve space

               /* Date inTimestamp = doc.getDate("IN_TIMESTAMP");
                if (inTimestamp != null) {
                    dto.setRequestTime(new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSSS a", Locale.ENGLISH).format(inTimestamp));
                }*/
                Date inTimestamp = doc.getDate("IN_TIMESTAMP");
                if (inTimestamp != null) {
                    dto.setRequestTime(
                            DateTimeFormatter.ofPattern("dd-MMM-yy hh.mm.ss.SSSSSSS a", Locale.ENGLISH)
                                    .withZone(ZoneOffset.UTC)
                                    .format(inTimestamp.toInstant())
                    );
                }



                result.add(dto);
            }

            return result;
        }

        @Override
        public List<Object> getMobileServiceNames(String databaseName, String serviceNamePattern) {
            MongoTemplate template = multiMongoConfig.getTemplate(databaseName);

            // Build query
            Query query = new Query();
            query.addCriteria(Criteria.where("BACKEND_SYSTEM_ID").is("ESB"));
            query.addCriteria(Criteria.where("SERVICE_NAME").regex(
                    Pattern.compile(serviceNamePattern, Pattern.CASE_INSENSITIVE))
            );
            System.out.println("Final Mongo Query: " + query);
            List<Document> docs = template.find(query, Document.class, "esbservices");

            return docs.stream()
                    .map(doc -> doc.getString("SERVICE_NAME"))
                    .collect(Collectors.toList());
        }

        @Override
        public List<MobileExceptionDTO> getMobileExceptions(String databaseName, String excTableName, String flowName, String fromDate, String toDate) {
            if (excTableName == null || excTableName.trim().isEmpty()) {
                throw new IllegalArgumentException("Collection (table) name must not be null or empty");
            }

            MongoTemplate mongoTemplate = multiMongoConfig.getTemplate(databaseName);
            Query query = new Query();

            // Time range filter
            Instant from = parseFlexibleDate(fromDate);
            if (from != null) {
                ZonedDateTime startOfDay = from.atZone(ZoneId.of("Africa/Cairo"))
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
                from = startOfDay.toInstant(); // convert to UTC
            }

            Instant to = parseFlexibleDate(toDate);
            if (to != null) {
                to = to.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS).minusMillis(1);
            }





            if (from != null && to != null) {
                query.addCriteria(Criteria.where("CREATED_DATE")
                        .gte(Date.from(from))
                        .lte(Date.from(to)) // include end of day
                );
            }


            // FLOW_NAME LIKE (case-insensitive)
            if (flowName != null && !flowName.isEmpty()) {
                String regexPattern = ".*" + Pattern.quote(flowName) + ".*";
                query.addCriteria(Criteria.where("FLOW_NAME").regex(Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE)));
            }
            System.out.println("Final Mongo Query: " + query);
            System.out.println("Parsed FROM: " + from);
            System.out.println("Parsed TO  : " + to);
            System.out.println("Now: " + Instant.now());

            List<Document> docs = mongoTemplate.find(query, Document.class, excTableName);

            List<MobileExceptionDTO> dtoList = new ArrayList<>();
            for (Document doc : docs) {
                MobileExceptionDTO dto = new MobileExceptionDTO();
                dto.setMsgId(doc.getString("MSG_ID"));
                dto.setRequestId(doc.getString("REQUEST_ID"));
                dto.setServiceId(doc.getString("SERVICE_ID"));
                dto.setFlowName(doc.getString("FLOW_NAME"));

                Date created = doc.getDate("CREATED_DATE");
                if (created != null) {
                    dto.setCreatedDate(new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSS a", Locale.ENGLISH)
                            .format(created).toUpperCase());
                }

                dto.setExceptionList(doc.getString("EXCEPTION_LIST"));
                dto.setErrorCode(doc.getString("ERROR_CODE"));
                dto.setErrorDesc(doc.getString("ERROR_DESC"));
                dto.setExecutionGroup(doc.getString("EXECUTION_GROUP"));
                dto.setBrokerName(doc.getString("BROKER_NAME"));

                dtoList.add(dto);
            }

            return dtoList;
        }


        //helper methods
        private Instant parseFlexibleDate(String input) {
            if (input == null || input.trim().isEmpty()) return null;

            List<DateTimeFormatter> formatters = List.of(
                    new DateTimeFormatterBuilder()
                            .parseCaseInsensitive()
                            .appendPattern("dd-MMM-yy hh.mm.ss.SSSSSSS a")
                            .toFormatter(Locale.ENGLISH),
                    new DateTimeFormatterBuilder()
                            .parseCaseInsensitive()
                            .appendPattern("dd-MMM-yy hh.mm.ss a")
                            .toFormatter(Locale.ENGLISH),
                    new DateTimeFormatterBuilder()
                            .parseCaseInsensitive()
                            .appendPattern("dd-MMM-yy hh.mm.ss.SSS a") // ✅ Add this
                            .toFormatter(Locale.ENGLISH),
                    new DateTimeFormatterBuilder()
                            .parseCaseInsensitive()
                            .appendPattern("dd-MMM-yy hh a")
                            .toFormatter(Locale.ENGLISH),
                    new DateTimeFormatterBuilder()
                            .parseCaseInsensitive()
                            .appendPattern("dd-MMM-yy")
                            .toFormatter(Locale.ENGLISH)
            );

            for (DateTimeFormatter formatter : formatters) {
                try {
                    LocalDateTime ldt = LocalDateTime.parse(input, formatter);
                    return ldt.atZone(ZoneId.of("Africa/Cairo")).toInstant(); // convert to UTC
                } catch (DateTimeParseException ignored) {
                }
            }

            throw new IllegalArgumentException("Could not parse date: " + input);
        }



        public List<MobileGlobalTRXDTO> getMobileGlobalTRXRoute(String databaseName, String tableName,
                                                                String fromDate, String toDate,
                                                                String mobileMSISDN, String mobileCustomerID, String mobileRequestID,
                                                                String mobileSubscriberID,
                                                                String eventKey1, String eventKey2, String eventKey3,
                                                                String eventKey4, String eventKey5) {
            return switch (databaseName.toUpperCase()) {
                case "MONGOMOBILEPROD" ->
                        serviceRoutingStore.mobileGlobalTRXForProd(databaseName, tableName, fromDate, toDate, mobileMSISDN,
                                mobileCustomerID, mobileRequestID, mobileSubscriberID, eventKey1, eventKey2, eventKey3, eventKey4, eventKey5);
                              default -> throw new IllegalArgumentException("Invalid database: " + databaseName);
            };
        }

        public List<Object> getMobileServiceNamesRoute(String databaseName, String serviceNamePattern) {

            return switch (databaseName.toUpperCase()) {
                case "MONGOMOBILEPROD" ->
                        serviceRoutingStore.getMobileServiceNamesForProd(databaseName,serviceNamePattern);
                default -> throw new IllegalArgumentException("Invalid database: " + databaseName);
            };
        }

        public List<MobileExceptionDTO> getMobileExceptionsRoute(String databaseName, String excTableName,
                                                     String flowName, String fromDate, String toDate){

            return switch (databaseName.toUpperCase()) {
                case "MONGOMOBILEPROD" ->
                        serviceRoutingStore.getMobileExceptionsForProd(databaseName,excTableName,flowName,fromDate,toDate);
                default -> throw new IllegalArgumentException("Invalid database: " + databaseName);
            };
        }



        }
