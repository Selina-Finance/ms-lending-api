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

package com.selina.lending.repository;

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.mapper.eligibility.EligibilityRequestMapper;
import com.selina.lending.exception.RemoteResourceProblemException;
import com.selina.lending.httpclient.eligibility.EligibilityApi;
import com.selina.lending.httpclient.eligibility.dto.request.EligibilityRequest;
import com.selina.lending.httpclient.eligibility.dto.response.EligibilityResponse;
import com.selina.lending.httpclient.selection.dto.response.Product;
import com.selina.lending.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EligibilityRepository {

    private final EligibilityApi eligibilityApi;
    private final TokenService tokenService;
    private final EligibilityRequestMapper eligibilityRequestMapper;

    public EligibilityRepository(EligibilityApi eligibilityApi, TokenService tokenService, EligibilityRequestMapper eligibilityRequestMapper) {
        this.eligibilityApi = eligibilityApi;
        this.tokenService = tokenService;
        this.eligibilityRequestMapper = eligibilityRequestMapper;
    }

    public EligibilityResponse getEligibility(QuickQuoteApplicationRequest request, List<Product> products) {
        try {
            var partnerAccountId = tokenService.retrievePartnerAccountId();
            var propertyDetails = request.getPropertyDetails();
            var eligibilityRequest = eligibilityRequestMapper.mapToPropertyDetails(partnerAccountId, request, products);
            log.info("Retrieving eligibility for property details [partnerAccountId={}] [postcode={}] [address={}] [buildingNumber={}] [buildingName={}]" +
                            "and applicant [incomes={}] [creditRisk={}]",
                    partnerAccountId, propertyDetails.getPostcode(), propertyDetails.getAddressLine1(), propertyDetails.getBuildingNumber(), propertyDetails.getBuildingName(),
                    eligibilityRequest.getApplicant().getIncomes(), eligibilityRequest.getApplicant().getCreditRisk());
            return eligibilityApi.getEligibility(eligibilityRequest);
        } catch (Exception ex) {
            log.error("Error retrieving eligibility for property details", ex);
            throw new RemoteResourceProblemException();
        }
    }
}
