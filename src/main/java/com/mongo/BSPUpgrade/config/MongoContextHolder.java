package com.mongo.BSPUpgrade.config;

public class MongoContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setDatabaseName(String databaseName) {
        CONTEXT.set(databaseName);
    }

    public static String getDatabaseName() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}

