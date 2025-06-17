package com.mongo.BSPUpgrade.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.mongo.BSPUpgrade.response.Response;
//import com.bsp.upgrade.data.response.ResponseData;
import com.mongo.BSPUpgrade.response.ResponseStatus;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Response> globalExceptionHandler(Exception ex, WebRequest wr) {
        System.out.println("MICKRY");

        Response response = new Response();

        ResponseStatus status = new ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value() + "", ex.getMessage());

        response.setResponseStatus(status);
        response.setResponseData(null);

        return new ResponseEntity<Response>(response,
                HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
