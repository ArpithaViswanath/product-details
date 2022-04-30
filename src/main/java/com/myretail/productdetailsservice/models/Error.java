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
public class Error {
    @JsonProperty("code")
    private String code;
    @JsonProperty("description")
    private String description;
}
