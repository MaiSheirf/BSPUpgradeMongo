//package com.mongo.BSPUpgrade.controller;
//
//import com.mongo.BSPUpgrade.helpers.ResponseHandler;
//import com.mongo.BSPUpgrade.response.Response;
//import com.mongo.BSPUpgrade.service.ServiceRouting;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Collections;
//import java.util.List;
//
//@RestController
//@RequestMapping("/mongo")
//@Slf4j
//public class MongoServiceINFController {
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private ServiceRouting serviceRouter;
//
//    @GetMapping("/{databaseName}/{status}")
//    public Response getServiceName(@PathVariable("databaseName") String databaseName,
//                                   @PathVariable("status") String status) {
//
//        logger.trace("Controller will serve func [{}] through DB [{}] with variable sent [{}]",
//                "getServiceName", databaseName, status);
//
//        List<String> getServiceNameList = serviceRouter.getServiceNamesByStatus(databaseName, status);
//
//        return ResponseHandler.handleResponse(Collections.singletonList(getServiceNameList), "getServiceNameList");
//    }
//}
