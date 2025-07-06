package com.mongo.BSPUpgrade.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MobileGlobalTRXRequestDTO {

    private String databaseName;
    private String auditTableName;
    private String fromDate;
    private String toDate;
    private String mobileMSISDN;
    private String mobileCustomerID;
    private String mobileRequestID;
    private String mobileSubscriberID;
    private String eventKey1;
    private String eventKey2;
    private String eventKey3;
    private String eventKey4;
    private String eventKey5;
}
