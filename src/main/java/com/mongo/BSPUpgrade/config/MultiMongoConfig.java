package com.mongo.BSPUpgrade.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiMongoConfig {

    private final MongoSettings mongoSettings;

    public MultiMongoConfig(MongoSettings mongoSettings) {
        this.mongoSettings = mongoSettings;
    }

    private final Map<String, MongoTemplate> templateMap = new HashMap<>();

    public MongoTemplate getTemplate(String dbKey) {
        return templateMap.computeIfAbsent(dbKey, this::createTemplate);
    }

    private MongoTemplate createTemplate(String dbKey) {
        String uri = mongoSettings.getValues().get(dbKey);
        MongoClient mongoClient = MongoClients.create(uri);
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient, getDatabaseNameFromUri(uri)));
    }

    private String getDatabaseNameFromUri(String uri) {
        return uri.substring(uri.lastIndexOf("/") + 1);
    }

}
