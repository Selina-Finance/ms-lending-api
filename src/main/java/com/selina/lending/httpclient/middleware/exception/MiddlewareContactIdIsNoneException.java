package com.selina.lending.httpclient.middleware.exception;

import com.selina.lending.httpclient.middleware.dto.error.MiddlewareError;

public class MiddlewareContactIdIsNoneException extends RuntimeException {

    public MiddlewareContactIdIsNoneException(MiddlewareError error) {
        super(error.getError());
    }
}
