package com.selina.lending.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.Collections.emptyList;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "sla")
public class SlaProperties {

    private List<Request> requests = emptyList();

    @Data
    public static class Request {

        @NotBlank
        private String name;

        @NotBlank
        private String httpMethod;

        @NotBlank
        private String urlPattern;

        @NotNull
        private Long timeout;
    }
}
