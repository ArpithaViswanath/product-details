package com.myretail.productdetailsservice.controller;

import com.myretail.productdetailsservice.cassandra.repository.PriceRepository;
import com.myretail.productdetailsservice.constants.ProductDetailsServiceConstants;
import com.myretail.productdetailsservice.models.ResponseBody;
import com.myretail.productdetailsservice.service.OrchestrationService;
import com.myretail.productdetailsservice.service.ProductDescriptionService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.ResponseEntity;
import io.swagger.annotations.ApiOperation;

@RestController
@Slf4j
@RequestMapping("/v1/products")
public class ProductDetailsController {

    @Autowired
    private OrchestrationService orchestrationService;

    @GetMapping(ProductDetailsServiceConstants.QUERY_PARAM_PRODUCT_ID)
    @ApiOperation("Fetches all the product details")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful Retrieval of Product Details")
            }
    )
    public ResponseEntity<ResponseBody> getProduct(@PathVariable(ProductDetailsServiceConstants.PRODUCT_ID) String productId) {
        try {
            ResponseBody responseBody = orchestrationService.getResponse(productId);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error occurred while processing request for product id={}, Exception={}", productId, ex);
            ResponseBody responseBody = orchestrationService.getResponseDuringExceptions(ex.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {

        }
    }
}
