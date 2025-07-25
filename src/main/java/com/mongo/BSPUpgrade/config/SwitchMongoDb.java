package com.mongo.BSPUpgrade.config;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SwitchMongoDb {

    String value();
}
