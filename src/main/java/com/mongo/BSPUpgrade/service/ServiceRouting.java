package com.mongo.BSPUpgrade.service;

import com.mongo.BSPUpgrade.config.MultiMongoConfig;
import com.mongo.BSPUpgrade.entity.MongoServiceInf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Configuration
@ComponentScan
public class ServiceRouting implements ServiceList {

    @Autowired
    private MultiMongoConfig multiMongoConfig;

    @Override
    public List<String> getServiceNamesByStatus(String databaseName, String status) {
        MongoTemplate template = multiMongoConfig.getTemplate(databaseName);

        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(status));

        List<MongoServiceInf> results = template.find(query, MongoServiceInf.class, "service_inf");

        return results.stream()
                .map(MongoServiceInf::getServiceName)
                .collect(Collectors.toList());
    }
}
