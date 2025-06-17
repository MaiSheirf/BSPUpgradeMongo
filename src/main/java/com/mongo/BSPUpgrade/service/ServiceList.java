package com.mongo.BSPUpgrade.service;

import java.util.List;

public interface ServiceList {

    List<String> getServiceNamesByStatus(String databaseName ,String status);
}
