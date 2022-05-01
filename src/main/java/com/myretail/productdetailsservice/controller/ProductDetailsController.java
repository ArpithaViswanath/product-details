package com.myretail.productdetailsservice.controller;

import com.myretail.productdetailsservice.constants.Constants;
import com.myretail.productdetailsservice.models.ProductDetailsRequest;
import com.myretail.productdetailsservice.models.ResponseBody;
import com.myretail.productdetailsservice.service.OrchestrationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import io.swagger.annotations.ApiOperation;

@RestController
@Slf4j
@RequestMapping("/v1/products")
public class ProductDetailsController {

    @Autowired
    private OrchestrationService orchestrationService;

    @GetMapping(Constants.QUERY_PARAM_PRODUCT_ID)
    @ApiOperation("Fetches all the product details")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful Retrieval of Product Details")
            }
    )
    public ResponseEntity<ResponseBody> getProduct(@PathVariable(Constants.PRODUCT_ID) String productId) {
        try {
            ResponseBody responseBody = orchestrationService.fetchProductDetails(productId);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error occurred while processing request for product id={}, Exception={}", productId, ex);
            ResponseBody responseBody = orchestrationService.getResponseDuringExceptions(ex.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(Constants.QUERY_PARAM_PRODUCT_ID)
    @ApiOperation("Updates the product details")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful Update of Product Details")
            }
    )
    public ResponseEntity<ResponseBody> updateProduct(@PathVariable(Constants.PRODUCT_ID) String productId,
                                                      @RequestBody ProductDetailsRequest requestBody) {
        try {
            ResponseBody responseBody = orchestrationService.updateProductDetails(productId, requestBody);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error occurred while processing request for product id={}, Exception={}", productId, ex);
            ResponseBody responseBody = orchestrationService.getResponseDuringExceptions(ex.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
