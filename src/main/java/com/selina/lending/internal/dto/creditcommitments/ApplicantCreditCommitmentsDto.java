/*
 * Copyright 2022 Selina Finance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.selina.lending.internal.dto.creditcommitments;

import com.selina.lending.api.validator.Conditional;
import com.selina.lending.api.validator.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Conditional(selected = "ignore", values = {"true"}, required = {"reasonToIgnore"})
public class ApplicantCreditCommitmentsDto {
    Long id;
    Boolean primaryApplicant;
    String experianId;
    String searchId;
    Integer creditScore;
    String message;
    String creditCheck;
    CreditCommitmentsDetailDto creditCommitments;
    Boolean ignore;
    @Schema(implementation = ReasonToIgnore.class)
    @EnumValue(enumClass = ReasonToIgnore.class)
    String reasonToIgnore;


    enum ReasonToIgnore {
        IS_SELF_FUNDING("Item is a self funding buy-to-let"),
        WILL_BE_REPAID("Item will be repaid/cleared - evidence to be provided"),
        IS_WRONGLY_ATTRIBUTED("Item is wrongly attributed"),
        IS_DISPUTED("Item is disputed - Experian to be updated"),
        TERM_LESS_SIX_MONTHS("Remaining term is less than 6 months");

        final String value;

        ReasonToIgnore(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
