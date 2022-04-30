package com.myretail.productdetailsservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseBody {
    @JsonProperty("error")
    private Error error;
    @JsonProperty("payload")
    private Payload payload;
}
