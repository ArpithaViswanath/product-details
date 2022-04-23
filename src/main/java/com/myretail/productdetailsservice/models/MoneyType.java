package com.myretail.productdetailsservice.models;

import java.math.BigDecimal;
import java.util.Currency;

public class MoneyType {

    private BigDecimal value;

    private Currency currencyCode;

    public MoneyType(BigDecimal value, Currency currencyCode) {
        this.value = value;
        this.currencyCode = currencyCode;
    }
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Currency getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(Currency currencyCode) {
        this.currencyCode = currencyCode;
    }
}
