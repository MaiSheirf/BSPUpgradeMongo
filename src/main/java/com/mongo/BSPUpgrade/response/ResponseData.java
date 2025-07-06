package com.mongo.BSPUpgrade.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.util.List;




@Data
public class ResponseData {

   // private String LoginData;
    private List<Object> MobileServicesNames;
    private List<Object> MobileKeyPaths;
    private List<Object> MobileExceptions;
    private List<Object> MobileGlobalTRX;
    private List<Object> MobileServiceJourney;
    private List<Object> MobileNTRANativeQuery;
    private List<Object> MobileRetrevieDelayTRX;
    private List<Object> MongoServiceName;

}
