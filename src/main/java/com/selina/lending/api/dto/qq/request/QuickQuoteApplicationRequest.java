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

package com.selina.lending.api.dto.qq.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.dip.request.ApplicationRequest;
import com.selina.lending.api.validator.MatchNumberOfApplicants;
import com.selina.lending.api.validator.MaximumOnePrimaryApplicant;
import com.selina.lending.httpclient.middleware.dto.qq.request.Partner;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@MatchNumberOfApplicants
public class QuickQuoteApplicationRequest extends ApplicationRequest {

    @NotNull
    @Size(message = "applicants is required, min = 1, max = 2", min = 1, max = 2)
    @Valid
    @MaximumOnePrimaryApplicant
    private List<@NotNull QuickQuoteApplicantDto> applicants;

    @NotNull
    @Valid
    private LoanInformationDto loanInformation;

    @NotNull
    @Valid
    private QuickQuotePropertyDetailsDto propertyDetails;

    private LeadDto lead;

    @JsonIgnore
    private Partner partner; //TODO must not reuse httpclient DTO

    @Valid
    private QuickQuoteFeesDto fees;

    private Boolean hasGivenConsentForMarketingCommunications;

    @JsonIgnore
    private String testGroupId;
}
