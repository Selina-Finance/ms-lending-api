package com.selina.lending.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@Configuration
public class ObjectMapperConfiguration implements InitializingBean {

    private final ObjectMapper objectMapper;

    public ObjectMapperConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterPropertiesSet() {
        objectMapper.registerModules(
                new ProblemModule(),
                new ConstraintViolationProblemModule()
        );
    }
}
