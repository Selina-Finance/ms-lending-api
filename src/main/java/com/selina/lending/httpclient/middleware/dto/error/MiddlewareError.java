package com.selina.lending.httpclient.middleware.dto.error;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MiddlewareError {

    String error;
}
