package com.mongo.BSPUpgrade.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class MongoAspect {

    @Pointcut("@annotation(com.bsp.upgrade.config.SwitchMongoDb)")
    public void switchMongoDb() {}

    @Before("switchMongoDb()")
    public void before(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        SwitchMongoDb annotation = method.getAnnotation(SwitchMongoDb.class);
        if (annotation != null) {
            MongoContextHolder.setDatabaseName(annotation.value());
        }
    }

    @After("switchMongoDb()")
    public void after() {
        MongoContextHolder.clear();
    }
}
