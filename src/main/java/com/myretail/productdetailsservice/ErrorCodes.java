package com.myretail.productdetailsservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCodes {

    PRODUCT_DESCRIPTION_NOT_FOUND("ERR001"), PRICE_NOT_FOUND("ERR002"), READ_TIMEOUT("ERR003");

    private String errorCode;

}
