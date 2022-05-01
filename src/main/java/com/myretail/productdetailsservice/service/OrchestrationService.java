package com.myretail.productdetailsservice.service;

import com.myretail.productdetailsservice.ErrorCodes;
import com.myretail.productdetailsservice.cassandra.models.ProductPrice;
import com.myretail.productdetailsservice.cassandra.repository.PriceRepository;
import com.myretail.productdetailsservice.constants.ProductDetailsServiceConstants;
import com.myretail.productdetailsservice.exceptions.PriceNotFoundException;
import com.myretail.productdetailsservice.models.*;
import com.myretail.productdetailsservice.models.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
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
            log.error("Unable to find the description for Product ID {}!, and exception is {}", productId, ex);
            Error error = new Error(ErrorCodes.PRODUCT_DESCRIPTION_NOT_FOUND.getErrorCode(), ErrorCodes.PRODUCT_DESCRIPTION_NOT_FOUND.name());
            responseBody.setPayload(new Payload(ProductDetailsServiceConstants.FAIL));
            responseBody.setError(error);
        } catch (WebClientRequestException ex) {
            log.error("Read timeout exception while connecting to Product Description service!");
            Error error = new Error(ErrorCodes.READ_TIMEOUT.getErrorCode(), ErrorCodes.READ_TIMEOUT.name());
            responseBody.setPayload(new Payload(ProductDetailsServiceConstants.FAIL));
            responseBody.setError(error);
        } catch (IllegalStateException ex) {
            log.error("Read timeout exception while connecting to Product Description service!");
            Error error = new Error(ErrorCodes.READ_TIMEOUT.getErrorCode(), ErrorCodes.READ_TIMEOUT.name());
            responseBody.setPayload(new Payload(ProductDetailsServiceConstants.FAIL));
            responseBody.setError(error);
        } catch (PriceNotFoundException ex) {
            log.error("Unable to find the Price for Product ID {}!, and exception is {}", productId, ex);
            Error error = new Error(ErrorCodes.PRICE_NOT_FOUND.getErrorCode(), ErrorCodes.PRICE_NOT_FOUND.name());
            responseBody.setPayload(new Payload(ProductDetailsServiceConstants.FAIL));
            responseBody.setError(error);
        }
        return responseBody;
    }

    public ResponseBody getResponseDuringExceptions(String message) {
        Error error = new Error(ProductDetailsServiceConstants.ERROR_CODE_RUNTIME_EXCEPTION, message);
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
        } else {
            throw new PriceNotFoundException();
        }
        if (!CollectionUtils.isEmpty(result)) {
            return new Payload(ProductDetailsServiceConstants.SUCCESS, result);
        }
        return null;
    }

}
