package com.myretail.productdetailsservice.cassandra.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Getter
@Setter
@Table(value = "product_price")
public class ProductPrice {

    @PrimaryKeyColumn(name = "product_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String productId;

    @CassandraType(type = Name.FLOAT)
    private String price;

}
