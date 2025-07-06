package com.mongo.BSPUpgrade.dto;

import lombok.Data;

@Data
public class MobileExceptionRequestDTO {

    private String databaseName;
    private String excTableName;
    private String flowName;
    private String fromDate;
    private String toDate;
}
