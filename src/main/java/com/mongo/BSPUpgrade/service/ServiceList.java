package com.mongo.BSPUpgrade.service;

import com.mongo.BSPUpgrade.dto.MobileExceptionDTO;
import com.mongo.BSPUpgrade.dto.MobileGlobalTRXDTO;

import java.util.List;

public interface ServiceList {

   // List<String> getServiceNamesByStatus(String databaseName ,String status);

    List<MobileGlobalTRXDTO> getMobileGlobalTRX(
            String databaseName, String tableName, String fromDate, String toDate,
            String msisdn, String customerId, String requestId, String subscriberId,
            String key1, String key2, String key3, String key4, String key5
    );

    List<Object> getMobileServiceNames(String databaseName, String serviceNamePattern);

    List<MobileExceptionDTO> getMobileExceptions(String databaseName, String excTableName,
                                                 String flowName, String fromDate, String toDate);

}
