package com.mongo.BSPUpgrade.controller;

import com.mongo.BSPUpgrade.helpers.ResponseHandler;
import com.mongo.BSPUpgrade.response.Response;
import com.mongo.BSPUpgrade.service.ServiceRouting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/MobileServiceNames")
public class MobileServiceNameController {

    @Autowired
    private ServiceRouting serviceRouter;

    @GetMapping("/{databaseName}/{serviceNamePattern}")
    public Response getMobileServiceNames(
            @PathVariable String databaseName,
            @PathVariable String serviceNamePattern) {
        log.trace("Controller will serve func [{}] through DB [{}] with variables sent [{}]",
                "MobileServiceNames", databaseName, serviceNamePattern);

        serviceNamePattern = ".*" + serviceNamePattern + ".*";
        List<Object> MobileServiceNames = serviceRouter.getMobileServiceNamesRoute(databaseName, serviceNamePattern);
        List<Object> wrappedList = new ArrayList<>(MobileServiceNames);

        return ResponseHandler.handleResponse(wrappedList, "MobileServiceNames");

    }
}
