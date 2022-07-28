/*
 *   Copyright 2022 Selina Finance
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package com.selina.lending.internal.dto;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ApplicationDecisionResponse {
    String id;
    String sourceAccount;
    String requestType;
    String sourceUserId;
    String sourceBrokerUserId;
    String externalApplicationId;
    String reference;
    String selectedOffer;
    String selectedProduct;
    Date createdDate;
    Date modifiedDate;
    String decision;
    String status;
    String currentStatusDescription;
    Date statusDate;
    boolean hasAcceptedTermsAndConditionsAndPrivacyPolicy;
    boolean hasConsentToSubmitFundingApplication;
    boolean hasGivenConsentForHardCreditCheck;
    boolean hasGivenConsentForIdVerification;
    boolean hasGivenConsentForMarketingCommunications;
    boolean hasCertifiedCurrentFinancialCircumstances;
    boolean canAddProductFee;
    boolean canCaseBeFunded;
    String decisionError;
    String decisionStatus;
    String applicationStage;
    String bankSortCode;
    String bankAccountNumber;
    String bankName;
    String nameOfAccountHolder;
    String initialDisbursalAmount;
    List<ApplicantDto> applicants;
    LoanInformationDto loanInformation;
    FeesDto fees;
    List<OfferDto> offers;
    List<ExpenditureDto> expenditure;
}
