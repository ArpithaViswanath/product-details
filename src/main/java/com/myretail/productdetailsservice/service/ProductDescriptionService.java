package com.myretail.productdetailsservice.service;

import com.myretail.productdetailsservice.constants.Constants;
import com.myretail.productdetailsservice.models.ProductDescriptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@Slf4j
public class ProductDescriptionService {

    private final WebClient webclient;

    public ProductDescriptionService(WebClient.Builder builder) {
         this.webclient = builder.baseUrl(Constants.PRODUCT_DESCRIPTION_SERVICE_BASE_URL).build();
    }

    /**
     * Makes a call to an external API to fetch the Product name using HTTP GET
     * @param productId
     * @return ProductDescription object which includes the Product ID and Product Name.
     */
    public ProductDescriptionResponse getProductDescription(String productId) {
        try {
            return webclient
                    .get()
                    .uri(Constants.V1_PRODUCT_DESCRIPTION_BY_ID, productId)
                    .retrieve()
//                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> handle4xxError(clientResponse))
                    .bodyToMono(ProductDescriptionResponse.class)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1))
                            .filter(e -> e instanceof WebClientRequestException))
                    .block(Duration.ofSeconds(2));
        } catch (WebClientResponseException ex) {
            log.error("Error Response Code is {} and the response body is {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientReponseException in getProductDescription ", ex);
            throw ex;
        } catch (WebClientRequestException ex) {
            log.error("WebClientRequestException in getProductDescription ", ex);
            throw ex;
        }  catch (IllegalStateException ex) {
            log.error("IllegalStateException in getProductDescription ", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Exception in getProductDescription ", ex);
            throw ex;
        }
    }

    /**
     * Makes a call to the external API to update the Product name using HTTP PUT
     * @param productId, productName
     * @return ProductDescription object which includes updated the Product ID and Product Name.
     */
    public ProductDescriptionResponse updateProductDescription(String productId, String productName) {
        try {
            return webclient
                    .put()
                    .uri(Constants.V1_PRODUCT_DESCRIPTION_BY_ID, productId)
                    .body(Mono.just(productName), String.class)
                    .retrieve()
                    .bodyToMono(ProductDescriptionResponse.class)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1))
                        .filter(e -> e instanceof WebClientRequestException))
                    .block(Duration.ofSeconds(2));
        } catch (WebClientResponseException ex) {
            log.error("Error Response Code is {} and the response body is {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientResponseException in getProductDescription ", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Exception in getProductDescription ", ex);
            throw ex;
        }
    }
}
