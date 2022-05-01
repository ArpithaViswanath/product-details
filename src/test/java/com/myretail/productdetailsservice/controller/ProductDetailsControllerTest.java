package com.myretail.productdetailsservice.controller;

import com.myretail.productdetailsservice.models.ProductDescriptionResponse;
import com.myretail.productdetailsservice.service.ProductDescriptionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProductDetailsControllerTest {

    @Autowired
    ProductDescriptionService productDescriptionService;

    @Test
    public void testGetProduct() {
        String productId = "1";
        ProductDescriptionResponse productDescription = productDescriptionService.getProductDescription("1");
        assertEquals(productDescription.getName(), productId);
    }

    @Test
    public void testGetProduct_notFound() {
        String productId = "11";
        ProductDescriptionResponse productDescription = productDescriptionService.getProductDescription(productId);
        assertThrows(WebClientResponseException.class, ()->productDescriptionService.getProductDescription(productId));
    }

    @Test
    public void testUpdateProduct() {
        String productId = "10";
        String productName = "Laptop";
        ProductDescriptionResponse productDescription = new ProductDescriptionResponse("10", "Laptop");
        ProductDescriptionResponse updatedProduct = productDescriptionService.updateProductDescription(productId, productName);
        assertEquals(updatedProduct.getId(), productDescription.getId());
        assertEquals(updatedProduct.getName(), productDescription.getName());
    }

}
