package com.mongo.BSPUpgrade.helpers;

import com.mongo.BSPUpgrade.response.Response;
import com.mongo.BSPUpgrade.response.ResponseData;
import com.mongo.BSPUpgrade.response.ResponseStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ResponseHandler {

    public static Response handleResponse(List<Object> data, String serviceName) {
        Response response = new Response();
        ResponseData responseData = generateServiceResponseData(data, serviceName);
        ResponseStatus status = new ResponseStatus("100", "Success");

        System.out.println(" data is  >>>>>>>>>>>>>>>>>>>>>>>>>>> " + data.size() );
        System.out.println(" data is  >>>>>>>>>>>>>>>>>>>>>>>>>>> " +data.isEmpty());
        if (data.isEmpty() || data.size() == 0) {
            // set empty error code Status
            status.setCode("101");
            status.setMsg("NO Data Found");
        }
        response.setResponseStatus(status);
        response.setResponseData(responseData);
        return response;
    }

    private static ResponseData generateServiceResponseData(List<Object> data, String serviceName) {
        ResponseData responseData = new ResponseData();

        if (serviceName.equals("MobileServiceNames")) {
            responseData.setMobileServicesNames(data);
        }
        if (serviceName.equals("MobileKeyPaths")) {
            responseData.setMobileKeyPaths(data);
        }
        if (serviceName.equals("MobileExceptions")) {
            responseData.setMobileExceptions(data);
        }
        if ("MobileGlobalTRX".equals(serviceName)) {
            responseData.setMobileGlobalTRX(data);
        }
        if (serviceName.equals("MobileServiceJourney")) {
            responseData.setMobileServiceJourney(data);
        }
        if (serviceName.equals("MobileNTRANAtiveQuery")) {
            responseData.setMobileNTRANativeQuery(data);
        }
        if (serviceName.equals("MobileRetrevieDelayTRX")) {
            responseData.setMobileRetrevieDelayTRX(data);
        }


        return responseData;
    }
}
