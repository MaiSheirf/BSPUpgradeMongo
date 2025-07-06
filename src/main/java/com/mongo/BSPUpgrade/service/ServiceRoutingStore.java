package com.mongo.BSPUpgrade.service;

import com.mongo.BSPUpgrade.config.MultiMongoConfig;
import com.mongo.BSPUpgrade.config.SwitchMongoDb;
import com.mongo.BSPUpgrade.dto.MobileExceptionDTO;
import com.mongo.BSPUpgrade.dto.MobileGlobalTRXDTO;
import com.mongo.BSPUpgrade.entity.MongoServiceInf;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class ServiceRoutingStore {

    private static final Logger log = LoggerFactory.getLogger(ServiceRoutingStore.class);

    @Autowired
    private MultiMongoConfig multiMongoConfig;

    @Autowired
    @Lazy
    private ServiceRouting serviceRouting;

    @SwitchMongoDb("MONGODEV")
    public List<MongoServiceInf> getServiceNameForMongoDev(String status, String databaseName) {
        MongoTemplate mongoTemplate = multiMongoConfig.getTemplate("MONGODEV");
        Query query = new Query(Criteria.where("status").is(status));
        return mongoTemplate.find(query, MongoServiceInf.class, "service_inf");
    }

    private Query buildAuditQuery(String databaseName, String tableName,
                                  String fromDate, String toDate,
                                  String mobileMSISDN, String mobileCustomerID, String mobileRequestID,
                                  String mobileSubscriberID,
                                  String eventKey1, String eventKey2, String eventKey3,
                                  String eventKey4, String eventKey5) {

        Query query = new Query();

        try {
            if (fromDate != null && toDate != null) {
                Instant from = Instant.parse(fromDate);
                Instant to = Instant.parse(toDate);
                query.addCriteria(Criteria.where("IN_TIMESTAMP").gte(from).lte(to));
            }
        } catch (DateTimeParseException e) {
            log.error("Failed to parse date (expected ISO 8601 format): ", e);
        }

        if (mobileMSISDN != null)
            query.addCriteria(Criteria.where("MSISDN").is(mobileMSISDN));

        if (mobileCustomerID != null)
            query.addCriteria(Criteria.where("CUSTOMER_ID").is(mobileCustomerID));

        if (mobileRequestID != null) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("REQUEST_ID").is(mobileRequestID),
                    Criteria.where(" REQUEST_ID").is(mobileRequestID) // handle potential typo
            ));
        }

        if (mobileSubscriberID != null)
            query.addCriteria(Criteria.where("SUBSCRIBER_ID").is(mobileSubscriberID));

        if (eventKey1 != null)
            query.addCriteria(Criteria.where("BUSINESS_KEY1").is(eventKey1));

        if (eventKey2 != null)
            query.addCriteria(Criteria.where("BUSINESS_KEY2").is(eventKey2));

        if (eventKey3 != null)
            query.addCriteria(Criteria.where("BUSINESS_KEY3").is(eventKey3));

        if (eventKey4 != null)
            query.addCriteria(Criteria.where("BUSINESS_KEY4").is(eventKey4));

        if (eventKey5 != null)
            query.addCriteria(Criteria.where("BUSINESS_KEY5").is(eventKey5));

        query.with(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "IN_TIMESTAMP"));

        if (tableName == null || tableName.trim().isEmpty()) {
            log.error("Table name is null or empty");
            throw new IllegalArgumentException("Collection (table) name must not be null or empty");
        }

        return query;
    }

    @SwitchMongoDb("MONGOMOBILEPROD")
    public List<MobileGlobalTRXDTO> mobileGlobalTRXForProd(String databaseName, String tableName,
                                                          String fromDate, String toDate,
                                                          String mobileMSISDN, String mobileCustomerID, String mobileRequestID,
                                                          String mobileSubscriberID,
                                                          String eventKey1, String eventKey2, String eventKey3,
                                                          String eventKey4, String eventKey5) {

        return serviceRouting.getMobileGlobalTRX(databaseName,tableName,fromDate,toDate,mobileMSISDN,mobileCustomerID,mobileRequestID
                ,mobileSubscriberID,eventKey1,eventKey2,eventKey3,eventKey4,eventKey5);
    }

    @SwitchMongoDb("MONGOMOBILEPROD")
    public List<Object> getMobileServiceNamesForProd(String databaseName, String serviceNamePattern) {

        return serviceRouting.getMobileServiceNames(databaseName,serviceNamePattern);
    }
    @SwitchMongoDb("MONGOMOBILEPROD")
    public List<MobileExceptionDTO> getMobileExceptionsForProd(String databaseName, String excTableName,
                                                        String flowName, String fromDate, String toDate){

        return serviceRouting.getMobileExceptions(databaseName,excTableName,flowName,fromDate,toDate);
    }




}
