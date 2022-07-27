package com.selina.lending.internal.api;

import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// TODO: this class should be replaced with FeignClient
@Slf4j
@Component
public class MiddlewareApi {

    public ApplicationResponse getApplicationById(String id) {
        if (true) {
            log.debug("====================> Executing remote call to middleware-api");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException("problem with middleware api");
        }

        return null;
    }
}
