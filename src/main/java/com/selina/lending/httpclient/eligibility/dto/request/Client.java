package com.selina.lending.httpclient.eligibility.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Client {

    private String id;
}
