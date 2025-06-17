package com.mongo.BSPUpgrade.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.util.List;



@Setter
@Data
public class ResponseData {

    private List<Object> AllTransactionsData;
    private List<Object> FullJourneyData;
    private List<Object>ServiceNamesData;
    private List<Object>KeyPathsData;
    private String LoginData;
    private List<Object>ServiceHitsPerDay;
    private List<Object> AllTransactionsDataWithoutSN;
    private List<Object> FullJourneyBYTXIDData;
    private List<Object> MobileServicesNames;
    private List<Object> MobileKeyPaths;
    private List<Object> MobileExceptions;
    private List<Object> MobileGlobalTRX;
    private List<Object> MobileServiceJourney;
    private List<Object> MobileNTRANativeQuery;
    private List<Object> MobileRetrevieDelayTRX;
    @Getter
    private List<Object> MongoServiceName;

}
