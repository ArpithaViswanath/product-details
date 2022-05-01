package com.myretail.productdetailsservice.exceptions;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException() {
        super();
    }
    public PriceNotFoundException(String message) {
        super(message);
    }
    public PriceNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
    public PriceNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
