package com.selina.lending.internal.exception;

public class KafkaSendEventException extends RuntimeException {

    public KafkaSendEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
