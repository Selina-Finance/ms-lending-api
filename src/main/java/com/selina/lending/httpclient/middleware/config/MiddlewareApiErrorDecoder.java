package com.selina.lending.httpclient.middleware.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selina.lending.httpclient.middleware.dto.error.MiddlewareError;
import com.selina.lending.httpclient.middleware.exception.MiddlewareContactIdIsNoneException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;

@Slf4j
public class MiddlewareApiErrorDecoder implements ErrorDecoder {

    private static final String UNEXPECTED_ERROR = "Unexpected error";
    private static final String ERROR_DESERIALIZING_MIDDLEWARE_ERROR_RESPONSE_RESPONSE = "Error deserializing the Middleware error";
    private static final String MIDDLEWARE_CONTACT_ID_IS_NONE_ERROR = "ContactIdIsNoneError";

    private final ErrorDecoder defaultErrorDecoder = new Default();
    private final ObjectMapper objectMapper;

    public MiddlewareApiErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        MiddlewareError error = getMiddlewareError(response);

        if (response.status() == HttpStatus.BAD_REQUEST.value()) {
            if (MIDDLEWARE_CONTACT_ID_IS_NONE_ERROR.equalsIgnoreCase(error.getError())) {
                return new MiddlewareContactIdIsNoneException(error);
            }
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }

    private MiddlewareError getMiddlewareError(Response response) {
        try {
            var middlewareErrorResponseBody = IOUtils.toString(response.body().asInputStream());
            log.warn("Middleware error response [response={}]", middlewareErrorResponseBody);
            return objectMapper.readValue(middlewareErrorResponseBody, MiddlewareError.class);
        } catch (Exception ex) {
            log.error(ERROR_DESERIALIZING_MIDDLEWARE_ERROR_RESPONSE_RESPONSE, ex);
            return MiddlewareError.builder()
                    .error(UNEXPECTED_ERROR)
                    .build();
        }
    }
}
