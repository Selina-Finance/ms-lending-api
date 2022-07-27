package com.selina.lending.internal.api;

import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import org.springframework.stereotype.Component;

// TODO: this class should be replaced with FeignClient
@Component
public class MiddlewareApi {

    public ApplicationResponse getApplicationById(String id){
        return null;
    }
}
