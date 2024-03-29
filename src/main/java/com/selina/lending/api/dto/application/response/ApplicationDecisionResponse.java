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

package com.selina.lending.api.dto.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.selina.lending.api.dto.common.ExpenditureDto;
import com.selina.lending.api.dto.common.FeesDto;
import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.common.LoanInformationDto;
import com.selina.lending.api.dto.common.OfferDto;
import com.selina.lending.api.dto.common.PropertyDetailsDto;
import lombok.Builder;
import lombok.Value;

import java.util.Date;
import java.util.List;

import static com.selina.lending.api.controller.SwaggerConstants.DATE_TIME_FORMAT;

@Builder
@Value
public class ApplicationDecisionResponse {
    String id;
    String sourceAccount;
    String requestType;
    String sourceUserId;
    String sourceBrokerUserId;
    String externalApplicationId;
    String brokerSubmitterEmail;
    String reference;
    String selectedOffer;
    String selectedProduct;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    Date createdDate;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    Date modifiedDate;
    String decision;
    String status;
    String currentStatusDescription;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    Date statusDate;
    Boolean hasAcceptedTermsAndConditionsAndPrivacyPolicy;
    Boolean hasConsentToSubmitFundingApplication;
    Boolean hasGivenConsentForHardCreditCheck;
    Boolean hasGivenConsentForIdVerification;
    Boolean hasGivenConsentForMarketingCommunications;
    Boolean hasCertifiedCurrentFinancialCircumstances;
    Boolean canAddProductFee;
    Boolean canCaseBeFunded;
    String decisionError;
    String decisionStatus;
    String applicationStage;
    String initialDisbursalAmount;
    List<ApplicantResponseDto> applicants;
    SalesforceDto salesforce;
    LoanInformationDto loanInformation;
    PropertyDetailsDto propertyDetails;
    FeesDto fees;
    List<OfferDto> offers;
    List<ExpenditureDto> expenditure;
    LeadDto lead;
    IntermediaryDto intermediary;
    UnderwritingDto underwriting;
}
