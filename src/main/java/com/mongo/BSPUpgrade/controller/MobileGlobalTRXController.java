package com.mongo.BSPUpgrade.controller;

import com.mongo.BSPUpgrade.dto.MobileGlobalTRXDTO;
import com.mongo.BSPUpgrade.dto.MobileGlobalTRXRequestDTO;
import com.mongo.BSPUpgrade.helpers.ResponseHandler;
import com.mongo.BSPUpgrade.response.Response;
import com.mongo.BSPUpgrade.service.ServiceRouting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("MobileGlobalTRX")
@Slf4j
public class MobileGlobalTRXController {

   // private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ServiceRouting serviceRouter;

    @PostMapping
    public Response getMobileGlobalTRX(@RequestBody MobileGlobalTRXRequestDTO filter) {

        log.trace("Incoming request: {}", filter);

        if (filter.getAuditTableName() == null || filter.getAuditTableName().trim().isEmpty()) {
            throw new IllegalArgumentException("auditTableName must not be null or empty");
        }

        log.trace("Controller will serve func [{}] through DB [{}] with variables sent [{}]",
                "MobileGlobalTRX",
                filter.getDatabaseName(),
                Stream.of(
                        filter.getAuditTableName(),
                        filter.getFromDate(),
                        filter.getToDate(),
                        filter.getMobileMSISDN(),
                        filter.getMobileCustomerID(),
                        filter.getMobileRequestID(),
                        filter.getMobileSubscriberID(),
                        filter.getEventKey1(),
                        filter.getEventKey2(),
                        filter.getEventKey3(),
                        filter.getEventKey4(),
                        filter.getEventKey5()
                ).filter(Objects::nonNull).collect(Collectors.joining(", "))
        );

        List<MobileGlobalTRXDTO> MobileGlobalTRX = serviceRouter.getMobileGlobalTRXRoute(
                filter.getDatabaseName(),
                filter.getAuditTableName(),
                filter.getFromDate(),
                filter.getToDate(),
                filter.getMobileMSISDN(),
                filter.getMobileCustomerID(),
                filter.getMobileRequestID(),
                filter.getMobileSubscriberID(),
                filter.getEventKey1(),
                filter.getEventKey2(),
                filter.getEventKey3(),
                filter.getEventKey4(),
                filter.getEventKey5()
        );

        List<Object> wrappedList = new ArrayList<>(MobileGlobalTRX);

        return ResponseHandler.handleResponse(wrappedList, "MobileGlobalTRX");
       // return ResponseHandler.handleResponse(Collections.singletonList(MobileGlobalTRX), "MobileGlobalTRX");
    }
}
