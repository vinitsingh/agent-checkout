package com.agnet.pay.dto.checkout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    private String type;
    private String param;
    @JsonProperty("content_type")
    private ContentType content_type;
    private String content;
    private Code code ;

}
