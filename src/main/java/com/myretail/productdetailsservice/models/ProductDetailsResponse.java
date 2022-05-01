package com.myretail.productdetailsservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsResponse {

    private String id;
    private String name;
    private MoneyType currentPrice;

}
