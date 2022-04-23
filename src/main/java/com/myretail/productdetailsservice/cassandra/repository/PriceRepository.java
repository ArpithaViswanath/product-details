package com.myretail.productdetailsservice.cassandra.repository;

import com.myretail.productdetailsservice.cassandra.models.ProductPrice;
import jdk.internal.org.objectweb.asm.Opcodes;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceRepository extends CassandraRepository<ProductPrice, String> {

    @AllowFiltering
    Optional<ProductPrice> findByProductId(String id);

}
