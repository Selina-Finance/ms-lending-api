package com.selina.lending.api.dto.qq.request;

import com.selina.lending.api.validator.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CreditRisk {

    @Schema(implementation = CreditRisk.ConductStatus.class, description = "Represents the type of a passed filter on an aggregator's side")
    @EnumValue(enumClass = CreditRisk.ConductStatus.class)
    String conductStatus;

    enum ConductStatus {
        PRE_APPROVED("Pre-approved"),
        STATUS_0("Status 0"),
        STATUS_1("Status 1");

        final String value;
        ConductStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
