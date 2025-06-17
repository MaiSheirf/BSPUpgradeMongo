package com.mongo.BSPUpgrade.response;

import lombok.Data;

@Data
public class ResponseStatus {

    private String Code;
    private String Msg;

    public ResponseStatus(String code, String msg) {
        this.Code = code;
        this.Msg = msg;
    }
}
