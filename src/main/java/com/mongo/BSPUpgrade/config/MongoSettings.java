package com.mongo.BSPUpgrade.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Setter
@Getter
@Component // OR add a @Bean config method as explained above
@ConfigurationProperties(prefix = "mongodb")
public class MongoSettings {
    private Map<String, String> values;

}
