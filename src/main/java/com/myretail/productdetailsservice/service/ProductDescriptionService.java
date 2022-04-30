package com.myretail.productdetailsservice.service;

import com.myretail.productdetailsservice.exceptions.ProductDataException;
import com.myretail.productdetailsservice.models.ProductDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

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
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error Response Code is {} and the response body is {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientReponseException in getProductDescription ", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Exception in getProductDescription ", ex);
            throw ex;
        }
    }

    private Mono<? extends Throwable> handle4xxError(ClientResponse clientResponse) {
        Mono<String> errorMessage = clientResponse.bodyToMono(String.class);
        return errorMessage.flatMap((messsage) -> {
            log.error("");
            throw new ProductDataException(messsage);
        });
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
