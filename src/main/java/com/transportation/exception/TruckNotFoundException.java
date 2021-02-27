package com.transportation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Truck not found")
public class TruckNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -9343989185167L;

    public TruckNotFoundException(String message) {
        super(message);
    }
}
