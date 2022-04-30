package com.myretail.productdetailsservice.service;

import com.myretail.productdetailsservice.cassandra.models.ProductPrice;
import com.myretail.productdetailsservice.cassandra.repository.PriceRepository;
import com.myretail.productdetailsservice.models.*;
import com.myretail.productdetailsservice.models.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrchestrationService {

    @Autowired
    private ProductDescriptionService productDescriptionService;

    @Autowired
    private PriceRepository priceRepository;

    public ResponseBody getResponse(String productId) {
        ResponseBody responseBody = new ResponseBody();
        try {
            responseBody.setPayload(constructResponsePayload(productId));
        } catch (WebClientResponseException ex) {
            Error error = new Error("ERR0001", "Product ID Not Found");
            responseBody.setError(error);
        }
        return responseBody;
    }

    public ResponseBody getResponseDuringExceptions(String message) {
        Error error = new Error("ERR0002", message);
        return new ResponseBody(error, null);
    }

    private Payload constructResponsePayload(String productId) throws WebClientResponseException {

        List<ProductDetails> result = null;
        ProductDescription productDescription = productDescriptionService.getProductDescription(productId);
        Optional<ProductPrice> priceRepositoryById = priceRepository.findByProductId(productId);
        if (priceRepositoryById.isPresent()) {
            String price = priceRepositoryById.get().getPrice();
            result = Collections.singletonList(
                    new ProductDetails(productDescription.getId(), productDescription.getName(),
                            new MoneyType(new BigDecimal(price), Currency.getInstance(Locale.US)))
            );
        }
        if (!CollectionUtils.isEmpty(result)) {
            return new Payload("Success", result);
        }
        return null;
    }

}
