package com.mongo.BSPUpgrade.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "esbaudit")
@Data
public class MongoEsbAudit {
    @Id
    private String id;
    private String requestId;
    private String msgId;
    private Date in_timestamp;
    private String flowName;
    private String msisdn;
    private String customerId;
    private String subscriberId;
    private String businessKey1;
    private String businessKey2;
    private String businessKey3;
    private String businessKey4;
    private String businessKey5;
}
