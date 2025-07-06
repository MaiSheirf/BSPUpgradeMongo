package com.mongo.BSPUpgrade.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
public class MobileGlobalTRXDTO {

    private String requestId;
    private String msgId;
    private String requestTime;
    private String flowName;

}
