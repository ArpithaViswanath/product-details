package com.myretail.productdetailsservice.models;

public class ProductDetails {

    private String id;

    private String name;

    private MoneyType currentPrice;

    public ProductDetails(String id, String name, MoneyType moneyType) {
        this.id = id;
        this.name = name;
        this.currentPrice = moneyType;
    }

    public MoneyType getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(MoneyType currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
