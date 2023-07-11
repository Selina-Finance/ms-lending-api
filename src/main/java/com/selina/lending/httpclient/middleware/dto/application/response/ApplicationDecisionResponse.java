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

package com.selina.lending.httpclient.middleware.dto.application.response;

import java.util.Date;
import java.util.List;

import com.selina.lending.httpclient.middleware.dto.common.Applicant;
import com.selina.lending.httpclient.middleware.dto.common.Expenditure;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import com.selina.lending.httpclient.middleware.dto.common.LoanInformation;
import com.selina.lending.httpclient.middleware.dto.common.Offer;
import com.selina.lending.httpclient.middleware.dto.common.PropertyDetails;
import lombok.Builder;
import lombok.Data;

/**
 * Once an application is saved, the retrieval of the application is returned in
 * this object.
 */
@Builder
@Data
public class ApplicationDecisionResponse {
    private String id;
    private String sourceAccount;
    private String applicationType;
    private String sourceUserId;
    private String sourceBrokerUserId;
    private String externalApplicationId;
    private String reference;
    private String selectedOffer;
    private String selectedProduct;
    private Date createdDate;
    private Date modifiedDate;
    private String decision;
    private String status;
    private String currentStatusDescription;
    private Date statusDate;
    private Boolean hasAcceptedTermsAndConditionsAndPrivacyPolicy;
    private Boolean hasConsentToSubmitFundingApplication;
    private Boolean hasGivenConsentForHardCreditCheck;
    private Boolean hasGivenConsentForIdVerification;
    private Boolean hasGivenConsentForMarketingCommunications;
    private Boolean hasCertifiedCurrentFinancialCircumstances;
    private Boolean canAddProductFee;
    private Boolean canCaseBeFunded;
    private String decisionError;
    private String decisionStatus;
    private String applicationStage;
    private String bankSortCode;
    private String bankAccountNumber;
    private String bankName;
    private String nameOfAccountHolder;
    private String initialDisbursalAmount;
    private Underwriting underwriting;
    private List<Applicant> applicants;
    private LoanInformation loanInformation;
    private PropertyDetails propertyDetails;
    private Salesforce salesforce;
    private Fees fees;
    private List<Offer> offers;
    private List<Expenditure> expenditure;
    private Lead lead;
    private Intermediary intermediary;
}
