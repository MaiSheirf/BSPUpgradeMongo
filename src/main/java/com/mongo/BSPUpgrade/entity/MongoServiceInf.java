package com.mongo.BSPUpgrade.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "service_inf")
@Data
public class MongoServiceInf {
    @Id
    private String id;
    private String serviceName;
    private String status;
}
