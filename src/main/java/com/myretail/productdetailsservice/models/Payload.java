package com.myretail.productdetailsservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payload {
    @JsonProperty("status")
    private String status;

    @JsonProperty("productDetails")
    private List<ProductDetails> productDetails;
}
