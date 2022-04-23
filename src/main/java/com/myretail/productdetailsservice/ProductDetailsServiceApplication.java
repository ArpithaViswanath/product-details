package com.myretail.productdetailsservice;

import com.myretail.productdetailsservice.cassandra.connection.DataStaxAstraProperties;
import com.myretail.productdetailsservice.cassandra.models.ProductPrice;
import com.myretail.productdetailsservice.cassandra.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.nio.file.Path;

@SpringBootApplication
public class ProductDetailsServiceApplication {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient.Builder getWebClientBuilder () {
        return WebClient.builder();
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductDetailsServiceApplication.class, args);
    }

    /**
     * This is necessary to have the Spring Boot app use the Astra secure bundle
     * to connect to the database
     */
    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

}
