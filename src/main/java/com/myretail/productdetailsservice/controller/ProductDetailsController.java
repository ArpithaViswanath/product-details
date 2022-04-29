package com.myretail.productdetailsservice.controller;

import com.myretail.productdetailsservice.cassandra.models.ProductPrice;
import com.myretail.productdetailsservice.cassandra.repository.PriceRepository;
import com.myretail.productdetailsservice.models.MoneyType;
import com.myretail.productdetailsservice.models.ProductDescription;
import com.myretail.productdetailsservice.models.ProductDetails;
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

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/products")
public class ProductDetailsController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private ProductDescriptionService productDescriptionService;

    @GetMapping("/{productId}")
    @ApiOperation("Fetches all the product details")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "SuccessFul Retrieval of Product Details")
            }
    )
    public ResponseEntity<List<ProductDetails>> getProduct(@PathVariable("productId") String productId) {

        ProductDescription productDescription = productDescriptionService.getProductDescription(productId);

        Optional<ProductPrice> priceRepositoryById = priceRepository.findByProductId(productId);
        if (priceRepositoryById.isPresent()) {
            String price = priceRepositoryById.get().getPrice();
            List result = Collections.singletonList(
                    new ProductDetails(productDescription.getId(), productDescription.getName(),
                            new MoneyType(new BigDecimal(price), Currency.getInstance(Locale.US)))
            );
            return new ResponseEntity<List<ProductDetails>>(result, HttpStatus.OK);
        } else {

        }
        return null;
    }
}
