package com.selina.lending.internal.service.application.domain;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Partner {
    String companyId;
    String subUnitId;
}
