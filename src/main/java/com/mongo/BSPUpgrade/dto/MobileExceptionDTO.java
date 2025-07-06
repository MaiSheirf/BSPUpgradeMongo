package com.mongo.BSPUpgrade.dto;


import lombok.Data;
@Data
public class MobileExceptionDTO {
    private String msgId;
    private String requestId;
    private String serviceId;
    private String flowName;
    private String createdDate;
    private String exceptionList;
    private String errorCode;
    private String errorDesc;
    private String executionGroup;
    private String brokerName;
}
