package com.mongo.BSPUpgrade.response;

import lombok.Data;

@Data
public class Response {

    private com.mongo.BSPUpgrade.response.ResponseStatus ResponseStatus;
    private com.mongo.BSPUpgrade.response.ResponseData ResponseData;
}
