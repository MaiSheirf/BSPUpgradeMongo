package com.mongo.BSPUpgrade.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiMongoConfig {

    private final MongoSettings mongoSettings;

    public MultiMongoConfig(MongoSettings mongoSettings) {
        this.mongoSettings = mongoSettings;
        System.out.println("Loaded Mongo Settings: " + mongoSettings.getValues());
    }


    private final Map<String, MongoTemplate> templateMap = new HashMap<>();

    // Optional: This method is used by Spring if injected elsewhere.
    @Bean(name = "defaultMongoTemplate")
    public MongoTemplate defaultMongoTemplate() {
        return getTemplate("default");
    }

    // You manually call this from services to get the correct template
    public MongoTemplate getTemplate(String dbKey) {
        return templateMap.computeIfAbsent(dbKey, this::createTemplate);
    }

    private MongoTemplate createTemplate(String dbKey) {
        String uri = mongoSettings.getValues().get(dbKey);
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabaseFactory factory =
                new SimpleMongoClientDatabaseFactory(mongoClient, getDatabaseNameFromUri(uri));
        return new MongoTemplate(factory);
    }

    private String getDatabaseNameFromUri(String uri) {
        return uri.substring(uri.lastIndexOf("/") + 1);
    }
}

