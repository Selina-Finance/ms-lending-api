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

import java.util.List;
import java.util.Optional;

import com.selina.lending.httpclient.middleware.dto.qqcf.request.QuickQuoteCFRequest;
import org.springframework.stereotype.Service;

import com.selina.lending.internal.service.LendingConstants;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.httpclient.middleware.dto.common.Address;
import com.selina.lending.httpclient.middleware.dto.common.Applicant;
import com.selina.lending.httpclient.middleware.dto.dip.request.ApplicationRequest;
import com.selina.lending.httpclient.middleware.dto.common.PropertyDetails;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MiddlewareRequestEnricher {

    protected static final String ADDRESS_TYPE_CURRENT = "current";
    private final TokenService tokenService;

    public MiddlewareRequestEnricher(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void enrichCreateQuickQuoteCFRequest(QuickQuoteCFRequest applicationRequest) {
        applicationRequest.setSourceAccount(tokenService.retrieveSourceAccount());
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
        setIsApplicantResidenceIfNotSet(applicationRequest);
        applicationRequest.setRunDecisioning(true);
    }

    private void enrichApplicationRequest(ApplicationRequest applicationRequest, boolean includeCreditCommitments) {
        applicationRequest.setSourceAccount(tokenService.retrieveSourceAccount());
        applicationRequest.setIncludeCreditCommitment(includeCreditCommitments);
        applicationRequest.setSource(LendingConstants.REQUEST_SOURCE);
        applicationRequest.setProductCode(LendingConstants.PRODUCT_CODE_ALL);
        applicationRequest.getApplicants().forEach(this::setIdentifier);
        setIsApplicantResidenceIfNotSet(applicationRequest);
    }

    private void setIsApplicantResidenceIfNotSet(ApplicationRequest applicationRequest) {
        PropertyDetails propertyDetails = applicationRequest.getPropertyDetails();
        if (propertyDetails.getIsApplicantResidence() == null) {
            try {
                var currentAddress = getPrimaryApplicantCurrentAddress(applicationRequest);
                currentAddress.ifPresent(
                        address -> propertyDetails.setIsApplicantResidence(isEquals(applicationRequest, address)));
            } catch (Exception e) {
                log.error("Unable to set isApplicantResidence in request ", e);
            }
        }
    }

    private boolean isEquals(ApplicationRequest applicationRequest, Address address) {
        return address.getPostcode().equals(applicationRequest.getPropertyDetails().getPostcode());
    }

    private Optional<Address> getPrimaryApplicantCurrentAddress(ApplicationRequest applicationRequest) {
        return applicationRequest.getApplicants()
                .stream()
                .filter(Applicant::getPrimaryApplicant)
                .findFirst()
                .map(applicant -> getAddress(applicant.getAddresses()))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private Optional<Address> getAddress(List<Address> addresses) {
        return addresses.size() == 1 ? Optional.of(addresses.get(0)) : addresses.stream()
                .filter(address -> address.getAddressType().equals(ADDRESS_TYPE_CURRENT))
                .findFirst();
    }

    private void setIdentifier(Applicant applicant) {
        applicant.setIdentifier(Boolean.TRUE.equals(applicant.getPrimaryApplicant()) ?  1 : 2);
    }
}
