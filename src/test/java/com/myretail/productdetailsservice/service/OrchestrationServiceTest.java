package com.myretail.productdetailsservice.service;

import com.myretail.productdetailsservice.ErrorCodes;
import com.myretail.productdetailsservice.constants.ProductDetailsServiceConstants;
import com.myretail.productdetailsservice.models.ResponseBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrchestrationServiceTest {

    @Autowired
    OrchestrationService orchestrationService;

    private static final String PRODUCT_ID_WITH_SUCCESS_RESPONSE = "1";
    private static final String PRODUCT_ID_WITH_NO_PRICE = "4";
    private static final String PRODUCT_ID_WITH_NO_PRODUCT_DESCRIPTION = "10";

    @Test
    public void testConstructResponsePayload_successResponse() {
        ResponseBody responseBody = orchestrationService.getResponse(PRODUCT_ID_WITH_SUCCESS_RESPONSE);
        assertEquals(responseBody.getPayload().getStatus(), ProductDetailsServiceConstants.SUCCESS);
    }

    @Test
    public void testConstructResponsePayload_priceNotFound() {
        ResponseBody responseBody = orchestrationService.getResponse(PRODUCT_ID_WITH_NO_PRICE);
        assertNull(responseBody.getPayload());
        assertEquals(responseBody.getError().getCode(), ErrorCodes.PRICE_NOT_FOUND.getErrorCode());
    }

    @Test
    public void testConstructResponsePayload_productDescriptionNotFound() {
        ResponseBody responseBody = orchestrationService.getResponse(PRODUCT_ID_WITH_NO_PRODUCT_DESCRIPTION);
        assertNull(responseBody.getPayload());
        assertEquals(responseBody.getError().getCode(), ErrorCodes.PRODUCT_DESCRIPTION_NOT_FOUND.getErrorCode());
    }

    @Test
    public void testConstructResponsePayload_productIdNull() {
        ResponseBody responseBody = orchestrationService.getResponse(null);
        assertNull(responseBody.getPayload());
        assertEquals(responseBody.getError().getCode(), ErrorCodes.PRODUCT_DESCRIPTION_NOT_FOUND.getErrorCode());
    }

    @Test
    public void testConstructResponsePayload_productDescriptionServiceReadTimeout() {
        ResponseBody responseBody = orchestrationService.getResponse(PRODUCT_ID_WITH_SUCCESS_RESPONSE);
        assertNull(responseBody.getPayload());
        assertEquals(responseBody.getError().getCode(), ErrorCodes.READ_TIMEOUT.getErrorCode());
    }

    @Test
    public void testConstructResponsePayload_priceRepositoryDown() {
        ResponseBody responseBody = orchestrationService.getResponse(PRODUCT_ID_WITH_SUCCESS_RESPONSE);
        assertNull(responseBody.getPayload());
        assertEquals(responseBody.getError().getCode(), ErrorCodes.PRICE_NOT_FOUND.getErrorCode());
    }
}
