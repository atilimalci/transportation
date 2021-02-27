package com.transportation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "City not found")
public class CityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -98918516723L;

    private static final String MESSAGE = "city not found with code ";

    public CityNotFoundException(String code) {
        super(MESSAGE + code);
    }
}
