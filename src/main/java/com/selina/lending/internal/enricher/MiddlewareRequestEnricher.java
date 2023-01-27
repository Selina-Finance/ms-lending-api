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

package com.selina.lending.internal.enricher;

import org.springframework.stereotype.Service;

import com.selina.lending.internal.dto.LendingConstants;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.internal.service.application.domain.Applicant;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;

@Service
public class MiddlewareRequestEnricher {

    private final TokenService tokenService;

    public MiddlewareRequestEnricher(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void enrichCreateDipCCApplicationRequest(ApplicationRequest applicationRequest) {
        enrichApplicationRequest(applicationRequest, true);
    }

    public void enrichCreateDipApplicationRequest(ApplicationRequest applicationRequest) {
        enrichApplicationRequest(applicationRequest, false);
        applicationRequest.setStageOverwrite(LendingConstants.STAGE_OVERWRITE);
    }

    public void enrichPatchApplicationRequest(ApplicationRequest applicationRequest) {
        applicationRequest.getApplicants().forEach(this::setIdentifier);
        applicationRequest.setRunDecisioning(true);
    }

    private void enrichApplicationRequest(ApplicationRequest applicationRequest, boolean includeCreditCommitments) {
        applicationRequest.setSourceAccount(tokenService.retrieveSourceAccount());
        applicationRequest.setIncludeCreditCommitment(includeCreditCommitments);
        applicationRequest.setSource(LendingConstants.REQUEST_SOURCE);
        applicationRequest.setProductCode(LendingConstants.PRODUCT_CODE_ALL);
        applicationRequest.getApplicants().forEach(this::setIdentifier);
    }

    private void setIdentifier(Applicant applicant) {
        applicant.setIdentifier(Boolean.TRUE.equals(applicant.getPrimaryApplicant()) ?  0 : 1);
    }
}
