package com.myretail.productdetailsservice.service;

import com.myretail.productdetailsservice.ErrorCodes;
import com.myretail.productdetailsservice.cassandra.models.ProductPrice;
import com.myretail.productdetailsservice.cassandra.repository.PriceRepository;
import com.myretail.productdetailsservice.constants.Constants;
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

    /**
     * Fetches product name and price details, and handles errors
     * @param productId
     * @return
     */
    public ResponseBody fetchProductDetails(String productId) {
        ResponseBody responseBody = new ResponseBody();
        try {
            responseBody.setPayload(fetchDetails(productId));
        } catch (WebClientResponseException ex) {
            log.error("Unable to find the description for Product ID {}!, and exception is {}", productId, ex);
            Error error = new Error(ErrorCodes.PRODUCT_DESCRIPTION_NOT_FOUND.getErrorCode(), ErrorCodes.PRODUCT_DESCRIPTION_NOT_FOUND.name());
            responseBody.setPayload(new Payload(Constants.FAIL));
            responseBody.setError(error);
        } catch (WebClientRequestException ex) {
            log.error("Read timeout exception while connecting to Product Description service!");
            Error error = new Error(ErrorCodes.READ_TIMEOUT.getErrorCode(), ErrorCodes.READ_TIMEOUT.name());
            responseBody.setPayload(new Payload(Constants.FAIL));
            responseBody.setError(error);
        } catch (IllegalStateException ex) {
            log.error("Read timeout exception while connecting to Product Description service!");
            Error error = new Error(ErrorCodes.READ_TIMEOUT.getErrorCode(), ErrorCodes.READ_TIMEOUT.name());
            responseBody.setPayload(new Payload(Constants.FAIL));
            responseBody.setError(error);
        } catch (PriceNotFoundException ex) {
            log.error("Unable to find the Price for Product ID {}!, and exception is {}", productId, ex);
            Error error = new Error(ErrorCodes.PRICE_NOT_FOUND.getErrorCode(), ErrorCodes.PRICE_NOT_FOUND.name());
            responseBody.setPayload(new Payload(Constants.FAIL));
            responseBody.setError(error);
        }
        return responseBody;
    }

    /**
     * Updates Product Name and Price for given Product ID, and handles errors
     * @param productId
     * @param requestBody - Product name and Product price
     * @return
     */
    public ResponseBody updateProductDetails(String productId, ProductDetailsRequest requestBody) {
        ResponseBody responseBody = new ResponseBody();
        try {
            responseBody.setPayload(updateDetails(productId, requestBody));
        } catch (WebClientResponseException ex) {
            log.error("Unable to find the description for Product ID {}!, and exception is {}", productId, ex);
            Error error = new Error(ErrorCodes.PRODUCT_DESCRIPTION_NOT_FOUND.getErrorCode(), ErrorCodes.PRODUCT_DESCRIPTION_NOT_FOUND.name());
            responseBody.setPayload(new Payload(Constants.FAIL));
            responseBody.setError(error);
        } catch (WebClientRequestException ex) {
            log.error("Read timeout exception while connecting to Product Description service!");
            Error error = new Error(ErrorCodes.READ_TIMEOUT.getErrorCode(), ErrorCodes.READ_TIMEOUT.name());
            responseBody.setPayload(new Payload(Constants.FAIL));
            responseBody.setError(error);
        } catch (IllegalStateException ex) {
            log.error("Read timeout exception while connecting to Product Description service!");
            Error error = new Error(ErrorCodes.READ_TIMEOUT.getErrorCode(), ErrorCodes.READ_TIMEOUT.name());
            responseBody.setPayload(new Payload(Constants.FAIL));
            responseBody.setError(error);
        } catch (PriceNotFoundException ex) {
            log.error("Unable to find the Price for Product ID {}!, and exception is {}", productId, ex);
            Error error = new Error(ErrorCodes.PRICE_NOT_FOUND.getErrorCode(), ErrorCodes.PRICE_NOT_FOUND.name());
            responseBody.setPayload(new Payload(Constants.FAIL));
            responseBody.setError(error);
        }
        return responseBody;
    }

    /**
     * 1) Calls external API to fetch product Name
     * 2) Fetches price from DB
     * @param productId
     * @return Payload
     */
    private Payload fetchDetails(String productId) {

        List<ProductDetailsResponse> result;
        ProductDescriptionResponse productDescription = productDescriptionService.getProductDescription(productId);
        Optional<ProductPrice> priceRepositoryById = priceRepository.findByProductId(productId);
        if (priceRepositoryById.isPresent()) {
            String price = priceRepositoryById.get().getPrice();
            result = Collections.singletonList(
                    new ProductDetailsResponse(productDescription.getId(), productDescription.getName(),
                            new MoneyType(new BigDecimal(price), Currency.getInstance(Locale.US)))
            );
            return new Payload(Constants.SUCCESS, result);
        } else {
            throw new PriceNotFoundException();
        }
    }

    /**
     * 1) Calls external API to update product Name
     * 2) Updates price in Cassandra DB
     * @param productId
     * @param requestBody - Product name and Product price
     * @return Payload
     */
    private Payload updateDetails(String productId, ProductDetailsRequest requestBody) {

        List<ProductDetailsResponse> result;
        ProductDescriptionResponse productDescription = productDescriptionService.updateProductDescription(productId, requestBody.getName());
        Optional<ProductPrice> priceRepositoryById = priceRepository.findByProductId(productId);
        if (priceRepositoryById.isPresent()) {
            ProductPrice priceToUpdate = priceRepositoryById.get();
            priceToUpdate.setPrice(requestBody.getCurrentPrice().getValue().toString());
            ProductPrice updatedPrice = priceRepository.save(priceToUpdate);
            result = Collections.singletonList(
                    new ProductDetailsResponse(productDescription.getId(), productDescription.getName(),
                            new MoneyType(new BigDecimal(updatedPrice.getPrice()), Currency.getInstance(Locale.US)))
            );
            return new Payload(Constants.SUCCESS, result);
        } else {
            throw new PriceNotFoundException();
        }
    }

    public ResponseBody getResponseDuringExceptions(String message) {
        Error error = new Error(Constants.ERROR_CODE_RUNTIME_EXCEPTION, message);
        return new ResponseBody(error, null);
    }

}
