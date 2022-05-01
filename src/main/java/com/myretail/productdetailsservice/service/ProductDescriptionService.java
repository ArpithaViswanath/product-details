package com.myretail.productdetailsservice.service;

import com.myretail.productdetailsservice.models.ProductDescription;
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

         this.webclient = builder.baseUrl("http://localhost:8082").build();
    }

    public ProductDescription getProductDescription(String productId) {
        try {
            return webclient
                    .get()
                    .uri("/productdescription/" + productId)
                    .retrieve()
//                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> handle4xxError(clientResponse))
                    .bodyToMono(ProductDescription.class)
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

    public ProductDescription updateProductDescription(ProductDescription productDescription) {
        try {
            return webclient
                    .post()
                    .uri("/productdescription")
                    .body(Mono.just(productDescription), ProductDescription.class)
                    .retrieve()
                    .bodyToMono(ProductDescription.class)
                    .block();
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
