package com.myretail.productdetailsservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyType {

    private BigDecimal value;
    private Currency currencyCode;

}
