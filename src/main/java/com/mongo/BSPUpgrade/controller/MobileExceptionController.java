package com.mongo.BSPUpgrade.controller;

import com.mongo.BSPUpgrade.dto.MobileExceptionDTO;
import com.mongo.BSPUpgrade.dto.MobileExceptionRequestDTO;
import com.mongo.BSPUpgrade.helpers.ResponseHandler;
import com.mongo.BSPUpgrade.response.Response;
import com.mongo.BSPUpgrade.service.ServiceRouting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/MobileExceptions")
public class MobileExceptionController {

    @Autowired
    private ServiceRouting serviceRouter;

    @PostMapping
    public Response getException(@RequestBody MobileExceptionRequestDTO exception) {

        log.trace("Controller will serve func [{}] through DB [{}] with variables sent [{}]",
                "MobileExceptions", exception.getDatabaseName() , exception.getExcTableName() , exception.getFlowName() , exception.getFromDate() , exception.getToDate());

        List<MobileExceptionDTO> MobileExceptions = serviceRouter.getMobileExceptionsRoute(
                exception.getDatabaseName() , exception.getExcTableName() , exception.getFlowName() , exception.getFromDate() , exception.getToDate()
        );

        // 👇 Wrap into a generic Object list to match ResponseHandler method signature
        List<Object> wrappedList = new java.util.ArrayList<>(MobileExceptions);

        return ResponseHandler.handleResponse(wrappedList, "MobileExceptions");
    }
    }

