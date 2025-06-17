package com.mongo.BSPUpgrade.service;

import com.mongo.BSPUpgrade.config.MultiMongoConfig;
import com.mongo.BSPUpgrade.config.SwitchMongoDb;
import com.mongo.BSPUpgrade.entity.MongoServiceInf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Slf4j
@Service
public class ServiceRoutingStore {

    @Autowired
    private MultiMongoConfig multiMongoConfig;

    @SwitchMongoDb("MONGODEV")
    public List<MongoServiceInf> getServiceNameForMongoDev(String status, String databaseName) {
        MongoTemplate mongoTemplate = multiMongoConfig.getTemplate("MONGODEV");

        Query query = new Query(Criteria.where("status").is(status));
        return mongoTemplate.find(query, MongoServiceInf.class, "service_inf");
    }
}
