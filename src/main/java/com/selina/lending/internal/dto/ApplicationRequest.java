/*
 *  Copyright 2022 Selina Finance
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.selina.lending.internal.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.selina.lending.api.validator.EnumValue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Data
public class ApplicationRequest {

    @Schema(implementation = RequestType.class)
    @EnumValue(enumClass = RequestType.class)
    private String requestType;
    private String applicationStage;

    @NotBlank
    @Schema(implementation = Source.class)
    @EnumValue(enumClass = Source.class)
    private String source;
    private String sourceClientId;
    private String sourceAccount;

    @NotBlank
    @Schema(implementation = ProductCode.class)
    @EnumValue(enumClass = ProductCode.class)
    private String productCode;
    private String reference;

    private String externalApplicationId;
    private List<ExpenditureDto> expenditure;

    enum Source {
        BROKER("Broker"),
        INTERNAL("Internal"),
        NIVO("Nivo"),
        QUICK_QUOTE("Quick Quote Form");

        final String value;

        Source(String value) {
            this.value = value;
        }

        String getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    enum RequestType {
        FULL_APPLICATION("FullApplication"),
        DIP("DIP"),
        LEGACY("Legacy"),
        QUICK_QUOTE("QuickQuote");

        final String value;

        RequestType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    enum ProductCode {
        ALL("All"),
        QQ01("QQ01"),
        VAR0004("Var0004"),
        VAR0005("Var0005"),
        VAR0006("Var0006"),
        VAR0007("Var0007"),
        VAR0008("Var0008"),
        VAR0009("Var0009"),
        VAR0019("Var0010"),
        FIX0004("Fix0004"),
        FIX0005("Fix0005"),
        FIX0006("Fix0006"),
        FIX0007("Fix0007"),
        FIX0008("Fix0008"),
        FIX0009("Fix0009"),
        FIX0010("Fix0010");
        final String value;

        ProductCode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }
}
