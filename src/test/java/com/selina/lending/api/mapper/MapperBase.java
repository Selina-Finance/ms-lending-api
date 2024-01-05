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

package com.selina.lending.api.mapper;

import com.selina.lending.api.dto.common.AddressDto;
import com.selina.lending.api.dto.common.ApplicantDto;
import com.selina.lending.api.dto.common.ChecklistDto;
import com.selina.lending.api.dto.common.EmploymentDto;
import com.selina.lending.api.dto.common.ErcDto;
import com.selina.lending.api.dto.common.ExpenditureDto;
import com.selina.lending.api.dto.common.FeesDto;
import com.selina.lending.api.dto.common.IncomeDto;
import com.selina.lending.api.dto.common.IncomeItemDto;
import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.common.LoanInformationDto;
import com.selina.lending.api.dto.common.OfferDto;
import com.selina.lending.api.dto.common.PreviousNameDto;
import com.selina.lending.api.dto.common.PriorChargesDto;
import com.selina.lending.api.dto.common.PropertyDetailsDto;
import com.selina.lending.api.dto.common.RequiredDto;
import com.selina.lending.api.dto.creditcommitments.request.ApplicantCreditCommitmentsDto;
import com.selina.lending.api.dto.creditcommitments.request.CreditCommitmentsDetailDto;
import com.selina.lending.api.dto.creditcommitments.request.DetailDto;
import com.selina.lending.api.dto.creditcommitments.request.PublicInformationDto;
import com.selina.lending.api.dto.creditcommitments.request.SystemDto;
import com.selina.lending.api.dto.creditcommitments.request.UpdateCreditCommitmentsRequest;
import com.selina.lending.api.dto.creditcommitments.request.UserDto;
import com.selina.lending.api.dto.dip.request.AdvancedLoanInformationDto;
import com.selina.lending.api.dto.dip.request.ApplicationRequest;
import com.selina.lending.api.dto.dip.request.DIPApplicantDto;
import com.selina.lending.api.dto.dip.request.DIPApplicationRequest;
import com.selina.lending.api.dto.dip.request.DIPPropertyDetailsDto;
import com.selina.lending.api.dto.dip.request.FacilityDto;
import com.selina.lending.api.dto.dip.response.DIPApplicationDto;
import com.selina.lending.api.dto.dip.response.PropertyDetailValueDto;
import com.selina.lending.api.dto.dipcc.request.DIPCCApplicationRequest;
import com.selina.lending.api.dto.dipcc.request.DIPCCPropertyDetailsDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.dto.qq.request.QuickQuoteFeesDto;
import com.selina.lending.api.dto.qq.request.QuickQuotePropertyDetailsDto;
import com.selina.lending.api.dto.qq.response.QuickQuoteResponse;
import com.selina.lending.api.dto.qqcf.request.QuickQuoteCFApplicantDto;
import com.selina.lending.api.dto.qqcf.request.QuickQuoteCFApplicationRequest;
import com.selina.lending.api.dto.qqcf.request.QuickQuoteCFPropertyDetailsDto;
import com.selina.lending.httpclient.adp.dto.response.QuickQuoteEligibilityDecisionResponse;
import com.selina.lending.httpclient.creditcommitments.dto.common.ApplicantCreditCommitments;
import com.selina.lending.httpclient.creditcommitments.dto.common.CreditCommitmentsDetail;
import com.selina.lending.httpclient.creditcommitments.dto.common.CreditPolicy;
import com.selina.lending.httpclient.creditcommitments.dto.common.Detail;
import com.selina.lending.httpclient.creditcommitments.dto.common.PublicInformation;
import com.selina.lending.httpclient.creditcommitments.dto.common.Summary;
import com.selina.lending.httpclient.creditcommitments.dto.common.System;
import com.selina.lending.httpclient.creditcommitments.dto.common.User;
import com.selina.lending.httpclient.creditcommitments.dto.common.VotersRoll;
import com.selina.lending.httpclient.creditcommitments.dto.response.CreditCommitment;
import com.selina.lending.httpclient.creditcommitments.dto.response.CreditCommitmentResponse;
import com.selina.lending.httpclient.eligibility.dto.response.EligibilityResponse;
import com.selina.lending.httpclient.eligibility.dto.response.PropertyInfo;
import com.selina.lending.httpclient.middleware.dto.application.response.ApplicationDecisionResponse;
import com.selina.lending.httpclient.middleware.dto.application.response.Intermediary;
import com.selina.lending.httpclient.middleware.dto.application.response.Lead;
import com.selina.lending.httpclient.middleware.dto.application.response.Salesforce;
import com.selina.lending.httpclient.middleware.dto.application.response.Underwriting;
import com.selina.lending.httpclient.middleware.dto.common.Address;
import com.selina.lending.httpclient.middleware.dto.common.Applicant;
import com.selina.lending.httpclient.middleware.dto.common.Checklist;
import com.selina.lending.httpclient.middleware.dto.common.CreditCheck;
import com.selina.lending.httpclient.middleware.dto.common.Document;
import com.selina.lending.httpclient.middleware.dto.common.Employment;
import com.selina.lending.httpclient.middleware.dto.common.Erc;
import com.selina.lending.httpclient.middleware.dto.common.Expenditure;
import com.selina.lending.httpclient.middleware.dto.common.Facility;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import com.selina.lending.httpclient.middleware.dto.common.Income;
import com.selina.lending.httpclient.middleware.dto.common.Incomes;
import com.selina.lending.httpclient.middleware.dto.common.LoanInformation;
import com.selina.lending.httpclient.middleware.dto.common.Offer;
import com.selina.lending.httpclient.middleware.dto.common.PreviousName;
import com.selina.lending.httpclient.middleware.dto.common.PriorCharges;
import com.selina.lending.httpclient.middleware.dto.common.PropertyDetails;
import com.selina.lending.httpclient.middleware.dto.common.Required;
import com.selina.lending.httpclient.middleware.dto.common.RuleOutcome;
import com.selina.lending.httpclient.middleware.dto.dip.response.Application;
import com.selina.lending.httpclient.middleware.dto.dip.response.ApplicationResponse;
import com.selina.lending.httpclient.middleware.dto.qq.request.Partner;
import com.selina.lending.httpclient.middleware.dto.qqcf.request.QuickQuoteCFRequest;
import com.selina.lending.httpclient.middleware.dto.qqcf.response.QuickQuoteCFResponse;
import com.selina.lending.httpclient.selection.dto.response.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.httpclient.adp.dto.response.Product;
import com.selina.lending.httpclient.selection.dto.response.ProductOffer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.selina.lending.service.LendingConstants.ACCEPT_DECISION;

public abstract class MapperBase {
    public static final String TITLE = "Mrs.";
    public static final String FIRST_NAME = "Sally";
    public static final String LAST_NAME = "Smith";
    public static final String EMAIL_ADDRESS = "sally.smith@someemail.com";
    public static final String GENDER = "Female";
    public static final String MOBILE_NUMBER = "07965234654";
    public static final Integer ESTIMATED_RETIREMENT_AGE = 65;
    public static final String NATIONALITY = "United Kingdom";
    public static final String DOB = "1975-03-12";
    public static final String EMPLOYER_NAME = "Employer name";
    public static final Double INCOME_AMOUNT = 15000.00;
    public static final String INCOME_TYPE = "Gross salary";
    public static final String LOAN_PURPOSE = "Home improvements";
    public static final Integer LOAN_AMOUNT = 50000;
    public static final int LOAN_TERM = 5;
    public static final double ALLOCATION_AMOUNT = 50000;
    public static final Double AFFORDABILITY_DEFICIT = 2100.0;
    public static final String ALLOCATION_PURPOSE = "Home improvements";
    public static final String DESIRED_TIME_LINE = "By 3 months";
    public static final Double ESTIMATED_VALUE = 590000.00;
    public static final Double ELIGIBILITY_ESTIMATED_VALUE = 400000.00;
    public static final String WHEN_LAST_PURCHASED = "1990-01-05";
    public static final Double PURCHASE_VALUE = 390000.00;
    public static final String PROPERTY_TYPE = "Detached house";
    public static final int NUMBER_OF_BEDROOMS = 4;
    public static final String DIP_APPLICATION_TYPE = "DIP";
    public static final String APPLICATION_ID = "123456789";
    public static final Date CREATED_DATE = Date.from(Instant.now());
    public static final String ADDRESS_LINE_1 = "address line 1";
    public static final String PREVIOUS_ADDRESS_LINE_1 = "Evergreen Terrace";
    public static final String ADDRESS_LINE_2 = "address line 2";
    public static final String ADDRESS_TYPE = "current";
    public static final String PREVIOUS_ADDRESS_TYPE = "previous";
    public static final String COUNTRY = "England";
    public static final String BUILDING_NUMBER = "10";
    public static final String PREVIOUS_BUILDING_NUMBER = "742";
    public static final String CITY = "a city";
    public static final String PREVIOUS_CITY = "Springfield";
    public static final String POSTCODE = "postcode";
    public static final int UDPRN = 1235;
    public static final String PO_BOX = "poBox";
    public static final String BUILDING_NAME = "building name";
    public static final String COUNTY = "county";
    public static final String FROM_DATE = "2000-01-21";
    public static final String PREVIOUS_FROM_DATE = "2000-01-01";
    public static final String PREVIOUS_TO_DATE = "2020-01-01";
    public static final String OFFER_ID = "offer123";
    public static final String PRODUCT_CODE = "All";
    public static final String PRODUCT_NAME = "Homeowner loan, Status 0";
    public static final String EXPENDITURE_TYPE = "Utilities";
    public static final Double EXPENDITURE_AMOUNT_DECLARED = 250.0;
    public static final String EXPENDITURE_FREQUENCY = "monthly";
    public static final String EXTERNAL_APPLICATION_ID = "uniqueCaseID";
    public static final String RULE_OUTCOME = "Granted";
    public static final String REQUIRED_PASSPORT = "Passport";
    public static final String EMPLOYED_STATUS = "Employed";
    public static final String MARRIED_STATUS = "Married";
    public static final Double FEE = 599.00;
    public static final String CREDIT_CHECK_SERVICE_USED = "Experian";
    public static final int CREDIT_SCORE = 900;
    public static final String CREDIT_CHECK_REF = "6HVRQLKGFH";
    public static final String DOCUMENT_TYPE = "passport";
    public static final Integer DETAIL_ID = 123;
    public static final String STATUS = "Active";
    public static final String UNDERWRITING_STAGE = "Underwriting Stage";
    public static final String SALESFORCE_OPPORTUNITY_ID = "0062z000003YtBkAAK";
    public static final String SALESFORCE_ACCOUNT_ID = "837312";
    public static final String INTERMEDIARY_FIRSTNAME = "Jim";
    public static final String FCA_NUMBER = "27127";
    public static final String UTM_CAMPAIGN = "utm campaign";
    public static final String UTM_SOURCE = "utm source";
    public static final String UTM_MEDIUM = "utm medium";
    public static final Double MONTHLY_PAYMENT = 1000.0;
    public static final String RESIDENTIAL_STATUS_OWNER = "Owner";
    public static final Double MAX_ERC = 500.0;
    public static final Double ERC_BALANCE = 50000.0;
    public static final Double ERC_AMOUNT = 250.0;
    public static final Double ERC_FEE = 0.02;
    public static final String HOMEOWNER_LOAN = "Homeowner Loan";
    public static final String HELOC = "HELOC";
    public static final String DECISION = "Accepted";
    public static final String OFFER_DECISION_ACCEPT = "Accept";
    public static final String OFFER_DECISION_DECLINE = "Decline";
    public static final String OFFER_VARIABLE_RATE_50_LTV = "Variable Rate - 50% LTV";
    public static final String OFFER_VARIANT = "2 Year Fixed";
    public static final Double TOTAL_AMOUNT_REPAID = 60352.20;
    public static final Double PROC_FEE = 123.45;
    public static final Double PRODUCT_FEE = 651.1;
    public static final Double BROKER_FEES_INCLUDED = 10.50;
    public static final Double BROKER_FEES_UPFRONT = 5.25;
    public static final Double INITIAL_RATE = 8.75;
    public static final Double INITIAL_PAYMENT = 411.08;
    public static final Integer INITIAL_TERM = 5;
    public static final Double REVERSION_PAYMENT = 5.0;
    public static final Double APRC = 9.77;

    public static final Boolean IS_APRC_HEADLINE = false;
    public static final Double REQUESTED_LOAN_AMOUNT = 50000.0;
    public static final Double OUTSTANDING_BALANCE = 25000.0;
    public static final Double BALANCE_CONSOLIDATED = 20000.0;
    public static final Double OTHER_DEBT_PAYMENTS = 5000.0;
    public static final Double MAX_BALANCE_ESIS = 100000.0;
    public static final String ERC_PROFILE = "5%, 4%, 3%, 2%, 1%";
    public static final String ERC_SHORT_CODE = "ERC_01";
    public static final String CODE = "HOL00750";
    public static final Double EAR = 9.39;
    public static final Double SVR = 9.0;
    public static final Double OFFER_BALANCE = 30000.0;
    public static final String DECISIONING_ACCEPT = "Decisioning Accept";
    public static final Integer REVERSION_TERM = 3;
    public static final Double MAX_LOAN_AMOUNT = 50000.0;
    public static final Date MODIFIED_DATE;
    public static final String SOURCE_CLIENT_ID = "a-client-id";
    public static final String CATEGORY_STATUS_0 = "Status 0";
    public static final String FILTER_PASSED_PRE_APPROVAL = "Pre-approval";
    public static final Double LTI = 1.25;
    public static final Double LTV_CAP = 0.50;
    public static final Double CLTV = 31.25;
    public static final Double NET_LTV = 0.3125;
    protected static final Double ARRANGEMENT_FEE = 1000.00;
    protected static final Double ARRANGEMENT_FEE_SELINA = 1000.00;
    protected static final Double ARRANGEMENT_FEE_DISCOUNT_SELINA = 1.0;
    protected static final String BROKER_SUBMITTER_EMAIL = "broker_submitter@email.co.uk";
    public static final int ERC_PERIOD_YEARS = 2;

    public static final Double ELIGIBILITY = 80.1;
    public static final String EXPIRY_DATE = "2022-11-08 18:41:10.805143";
    public static final Double DTIR = 0.16635784011163915;
    public static final Double BBR = 2.25;
    public static final Double INITIAL_MARGIN = 6.5;
    public static final Double POUND_PAID_PER_BORROWED = 2.49;
    public static final Double MONTHLY_PAYMENT_STRESSED = 535.8718926064447;
    public static final String DECISION_AML = "Accept";
    public static final String DECISION_FRAUD = "Accept";
    public static final Double BASE_RATE_STRESSED = 5.75;
    public static final Double APRC_STRESSED = 13.29;
    public static final Double PROPERTY_VALUATION = 160000.0;
    public static final Double INITIAL_RATE_MINIMUM = 8.75;
    public static final Double REVERSION_MARGIN = 6.55;
    public static final Double REVERSION_RATE_MINIMUM = 8.72;
    public static final Integer DRAWDOWN_TERM = 5;
    public static final Double ESIS_LTV_CAPPED_BALANCE = 80000.0;
    public static final Double INCOME_PRIMARY_APPLICANT = 40000.0;
    public static final Double INCOME_JOINT_APPLICANT = 0.0;
    public static final Integer DAYS_UNTIL_INITIAL_DRAWDOWN = 7;
    public static final Integer FIXED_TERM_YEARS = 3;
    public static final Double MINIMUM_INITIAL_DRAWDOWN = 1000.0;
    public static final Double OFFER_VALIDITY = 30.0;
    public static final Double ESIS_LOAN_AMOUNT = 50000.0;
    public static final Double NET_LOAN_AMOUNT = 50000.0;
    public static final String SUB_UNIT_ID = "subUnitId";
    public static final String COMPANY_ID = "companyId";

    static {
        try {
            MODIFIED_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-01-22 11:00:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected ApplicationRequest getApplicationRequestDto() {
        return ApplicationRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .expenditure(List.of(getExpenditureDto()))
                .build();
    }

    protected DIPApplicationRequest getDIPApplicationRequestDto() {
        return DIPApplicationRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .sourceClientId(SOURCE_CLIENT_ID)
                .expenditure(List.of(getExpenditureDto()))
                .applicants(List.of(getDIPApplicantDto()))
                .loanInformation(getAdvancedLoanInformationDto())
                .propertyDetails(getDIPPropertyDetailsDto())
                .fees(getFeesDto())
                .brokerSubmitterEmail(BROKER_SUBMITTER_EMAIL)
                .build();
    }


    protected DIPCCApplicationRequest getDIPCCApplicationRequestDto() {
        return DIPCCApplicationRequest.builder()
                .sourceClientId(SOURCE_CLIENT_ID)
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .brokerSubmitterEmail(BROKER_SUBMITTER_EMAIL)
                .expenditure(List.of(getExpenditureDto()))
                .applicants(List.of(getDIPApplicantDto()))
                .loanInformation(getAdvancedLoanInformationDto())
                .propertyDetails(getDIPCCPropertyDetailsDto())
                .fees(getFeesDto())
                .build();
    }

    protected QuickQuoteCFApplicationRequest getQuickQuoteCFApplicationRequestDto() {
        return QuickQuoteCFApplicationRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .expenditure(List.of(getExpenditureDto()))
                .loanInformation(getLoanInformationDto())
                .propertyDetails(getQuickQuoteCFPropertyDetailsDto())
                .applicants(List.of(getQuickQuoteCFApplicantDto()))
                .fees(getFeesDto())
                .build();
    }

    protected QuickQuoteCFPropertyDetailsDto getQuickQuoteCFPropertyDetailsDto() {
        return QuickQuoteCFPropertyDetailsDto.builder()
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .buildingName(BUILDING_NAME)
                .buildingNumber(BUILDING_NUMBER)
                .city(CITY)
                .country(COUNTRY)
                .county(COUNTY)
                .postcode(POSTCODE)
                .estimatedValue(ESTIMATED_VALUE)
                .whenLastPurchased(WHEN_LAST_PURCHASED)
                .purchaseValue(PURCHASE_VALUE)
                .numberOfPriorCharges(1)
                .priorCharges(getPriorChargesDto())
                .build();
    }

    protected QuickQuoteCFApplicantDto getQuickQuoteCFApplicantDto() {
        return QuickQuoteCFApplicantDto.builder()
                .title(TITLE)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .gender(GENDER)
                .emailAddress(EMAIL_ADDRESS)
                .mobileNumber(MOBILE_NUMBER)
                .addresses(List.of(getAddressDto()))
                .dateOfBirth(DOB)
                .income(getIncomeDto())
                .residentialStatus(RESIDENTIAL_STATUS_OWNER)
                .employment(getEmploymentDto())
                .build();
    }

    protected QuickQuoteApplicationRequest getQuickQuoteApplicationRequestDto() {
        return QuickQuoteApplicationRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .expenditure(List.of(getExpenditureDto()))
                .loanInformation(getQQLoanInformationDto())
                .propertyDetails(getQuickQuotePropertyDetailsDto())
                .applicants(getQuickQuoteApplicantDtoList())
                .lead(getLeadDto())
                .partner(getPartner())
                .build();
    }

    protected QuickQuoteApplicationRequest getQuickQuoteApplicationRequestWithFeesDto() {
        return QuickQuoteApplicationRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .expenditure(List.of(getExpenditureDto()))
                .loanInformation(getQQLoanInformationDto())
                .propertyDetails(getQuickQuotePropertyDetailsDto())
                .applicants(getQuickQuoteApplicantDtoList())
                .lead(getLeadDto())
                .fees(getQuickQuoteFeesDto())
                .build();
    }

    protected com.selina.lending.api.dto.qq.request.LoanInformationDto getQQLoanInformationDto() {
        return com.selina.lending.api.dto.qq.request.LoanInformationDto.builder()
                .loanPurpose(LOAN_PURPOSE)
                .requestedLoanAmount(LOAN_AMOUNT)
                .numberOfApplicants(1)
                .requestedLoanTerm(LOAN_TERM)
                .desiredTimeLine(DESIRED_TIME_LINE)
                .build();
    }

    private List<QuickQuoteApplicantDto> getQuickQuoteApplicantDtoList() {
        List<QuickQuoteApplicantDto> list = new ArrayList<>();
        list.add(getQuickQuoteApplicantDto());
        return list;
    }

    protected UpdateCreditCommitmentsRequest getUpdateCreditCommitmentsRequest() {
        return UpdateCreditCommitmentsRequest.builder()
                .applicants(List.of(getApplicantCreditCommitmentDto()))
                .build();
    }

    private ApplicantCreditCommitmentsDto getApplicantCreditCommitmentDto() {
        return ApplicantCreditCommitmentsDto.builder().publicInformation(getPublicInformationDto()).creditCommitments(getCreditCommitmentsDetailDto()).primaryApplicant(true).id(1L).build();
    }

    public CreditCommitmentsDetailDto getCreditCommitmentsDetailDto() {
        return CreditCommitmentsDetailDto.builder()
                .system(getSystemDto()).user(getUserDto()).build();
    }

    public PublicInformationDto getPublicInformationDto() {
        return PublicInformationDto.builder().user(getUserDto()).build();
    }

    private SystemDto getSystemDto() {
        return SystemDto.builder().detail(List.of(getDetailDto())).build();
    }

    private UserDto getUserDto() {
        return UserDto.builder().detail(List.of(getDetailDto())).build();
    }

    private DetailDto getDetailDto() {
        return DetailDto.builder().id(DETAIL_ID).status(STATUS).build();
    }

    protected FeesDto getFeesDto() {
        return FeesDto.builder()
                .adviceFee(FEE)
                .thirdPartyFee(FEE)
                .commissionFee(FEE)
                .valuationFee(FEE)
                .isAddAdviceFeeToLoan(true)
                .isAddArrangementFeeToLoan(true)
                .isAddCommissionFeeToLoan(true)
                .isAddValuationFeeToLoan(true)
                .isAddThirdPartyFeeToLoan(true)
                .arrangementFee(ARRANGEMENT_FEE)
                .isAddProductFeesToFacility(true)
                .isAddIntermediaryFeeToLoan(false)
                .build();
    }

    protected QuickQuoteFeesDto getQuickQuoteFeesDto() {
        return QuickQuoteFeesDto.builder()
                .adviceFee(FEE)
                .thirdPartyFee(FEE)
                .commissionFee(FEE)
                .valuationFee(FEE)
                .isAddAdviceFeeToLoan(true)
                .isAddArrangementFeeToLoan(true)
                .isAddCommissionFeeToLoan(true)
                .isAddValuationFeeToLoan(true)
                .isAddThirdPartyFeeToLoan(true)
                .arrangementFee(ARRANGEMENT_FEE)
                .isAddProductFeesToFacility(true)
                .isAddIntermediaryFeeToLoan(false)
                .isAddArrangementFeeSelinaToLoan(true)
                .build();
    }

    protected ExpenditureDto getExpenditureDto() {
        return ExpenditureDto.builder()
                .frequency(EXPENDITURE_FREQUENCY)
                .amountDeclared(EXPENDITURE_AMOUNT_DECLARED)
                .expenditureType(EXPENDITURE_TYPE)
                .build();
    }

    protected AddressDto getAddressDto() {
        return AddressDto.builder()
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .addressType(ADDRESS_TYPE)
                .country(COUNTRY)
                .buildingNumber(BUILDING_NUMBER)
                .buildingName(BUILDING_NAME)
                .city(CITY)
                .postcode(POSTCODE)
                .udprn(UDPRN)
                .poBox(PO_BOX)
                .county(COUNTY)
                .fromDate(LocalDate.parse(FROM_DATE))
                .build();
    }

    protected AddressDto getPreviousAddressDto() {
        return AddressDto.builder()
                .addressLine1(PREVIOUS_ADDRESS_LINE_1)
                .addressType(PREVIOUS_ADDRESS_TYPE)
                .buildingNumber(PREVIOUS_BUILDING_NUMBER)
                .city(PREVIOUS_CITY)
                .postcode(POSTCODE)
                .fromDate(LocalDate.parse(PREVIOUS_FROM_DATE))
                .toDate(LocalDate.parse(PREVIOUS_TO_DATE))
                .build();
    }

    protected ApplicantDto getApplicantDto() {
        return ApplicantDto.builder()
                .title(TITLE)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .gender(GENDER)
                .emailAddress(EMAIL_ADDRESS)
                .mobileNumber(MOBILE_NUMBER)
                .addresses(List.of(getAddressDto()))
                .dateOfBirth(DOB)
                .income(getIncomeDto())
                .build();
    }

    protected QuickQuoteApplicantDto getQuickQuoteApplicantDto() {
        return QuickQuoteApplicantDto.builder()
                .primaryApplicant(true)
                .title(TITLE)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .gender(GENDER)
                .emailAddress(EMAIL_ADDRESS)
                .mobileNumber(MOBILE_NUMBER)
                .addresses(List.of(getAddressDto()))
                .dateOfBirth(DOB)
                .income(getIncomeDto())
                .residentialStatus(RESIDENTIAL_STATUS_OWNER)
                .employment(getEmploymentDto())
                .filterPassed(FILTER_PASSED_PRE_APPROVAL)
                .build();
    }

    protected EmploymentDto getEmploymentDto() {
        return EmploymentDto.builder().employmentStatus(EMPLOYED_STATUS).employerName(EMPLOYER_NAME)
                .inProbationPeriod(false)
                .build();
    }

    private LeadDto getLeadDto() {
        return LeadDto.builder()
                .utmCampaign(UTM_CAMPAIGN)
                .utmMedium(UTM_MEDIUM)
                .utmSource(UTM_SOURCE)
                .build();
    }

    protected DIPApplicantDto getDIPApplicantDto() {
        return DIPApplicantDto.builder()
                .title(TITLE)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .gender(GENDER)
                .emailAddress(EMAIL_ADDRESS)
                .mobileNumber(MOBILE_NUMBER)
                .maritalStatus(MARRIED_STATUS)
                .applicantUsedAnotherName(false)
                .primaryApplicant(true)
                .estimatedRetirementAge(ESTIMATED_RETIREMENT_AGE)
                .addresses(List.of(getAddressDto()))
                .nationality(NATIONALITY)
                .dateOfBirth(DOB)
                .employment(getEmploymentDto())
                .income(getIncomeDto())
                .previousNames(List.of(getPreviousNameDto()))
                .build();
    }

    protected IncomeDto getIncomeDto() {
        return IncomeDto.builder().income(List.of(IncomeItemDto.builder().amount(INCOME_AMOUNT).type(INCOME_TYPE).build())).build();
    }

    protected PreviousNameDto getPreviousNameDto() {
        return PreviousNameDto.builder().firstName(FIRST_NAME).lastName(LAST_NAME).title(TITLE).build();
    }

    protected LoanInformationDto getLoanInformationDto() {
        return LoanInformationDto.builder()
                .loanPurpose(LOAN_PURPOSE)
                .requestedLoanAmount(LOAN_AMOUNT)
                .numberOfApplicants(1)
                .requestedLoanTerm(LOAN_TERM)
                .desiredTimeLine(DESIRED_TIME_LINE)
                .build();
    }

    protected AdvancedLoanInformationDto getAdvancedLoanInformationDto() {
        return AdvancedLoanInformationDto.builder()
                .loanPurpose(LOAN_PURPOSE)
                .requestedLoanAmount(LOAN_AMOUNT)
                .numberOfApplicants(1)
                .requestedLoanTerm(LOAN_TERM)
                .facilities(List.of(getFacilityDto()))
                .desiredTimeLine(DESIRED_TIME_LINE)
                .build();
    }

    protected FacilityDto getFacilityDto() {
        return FacilityDto.builder().allocationAmount(ALLOCATION_AMOUNT).allocationPurpose(ALLOCATION_PURPOSE).build();
    }

    protected QuickQuotePropertyDetailsDto getQuickQuotePropertyDetailsDto() {
        return QuickQuotePropertyDetailsDto.builder()
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .buildingName(BUILDING_NAME)
                .buildingNumber(BUILDING_NUMBER)
                .city(CITY)
                .country(COUNTRY)
                .county(COUNTY)
                .postcode(POSTCODE)
                .estimatedValue(ESTIMATED_VALUE)
                .whenLastPurchased(WHEN_LAST_PURCHASED)
                .purchaseValue(PURCHASE_VALUE)
                .numberOfPriorCharges(1)
                .priorCharges(getPriorChargesDto())
                .build();
    }

    protected PropertyDetailsDto getPropertyDetailsDto() {
        return PropertyDetailsDto.builder().addressLine1(ADDRESS_LINE_1).addressLine2(ADDRESS_LINE_2).buildingName(
                BUILDING_NAME).buildingNumber(BUILDING_NUMBER).city(CITY).country(COUNTRY).county(COUNTY).postcode(
                POSTCODE).estimatedValue(ESTIMATED_VALUE).build();
    }

    protected PropertyDetailValueDto getPropertyDetailValueDto() {
        return PropertyDetailValueDto.builder().estimatedValue(ESTIMATED_VALUE).build();
    }

    protected DIPPropertyDetailsDto getDIPPropertyDetailsDto() {
        return DIPPropertyDetailsDto.builder()
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .buildingName(BUILDING_NAME)
                .buildingNumber(BUILDING_NUMBER)
                .city(CITY)
                .country(COUNTRY)
                .county(COUNTY)
                .postcode(POSTCODE)
                .estimatedValue(ESTIMATED_VALUE)
                .purchaseValue(PURCHASE_VALUE)
                .propertyType(PROPERTY_TYPE)
                .numberOfBedrooms(NUMBER_OF_BEDROOMS)
                .hasAGarage(true)
                .whenLastPurchased(WHEN_LAST_PURCHASED)
                .numberOfPriorCharges(1)
                .priorCharges(getPriorChargesDto())
                .build();
    }


    protected DIPCCPropertyDetailsDto getDIPCCPropertyDetailsDto() {
        return DIPCCPropertyDetailsDto.builder()
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .buildingName(BUILDING_NAME)
                .buildingNumber(BUILDING_NUMBER)
                .city(CITY)
                .country(COUNTRY)
                .county(COUNTY)
                .postcode(POSTCODE)
                .estimatedValue(ESTIMATED_VALUE)
                .purchaseValue(PURCHASE_VALUE)
                .propertyType(PROPERTY_TYPE)
                .numberOfBedrooms(NUMBER_OF_BEDROOMS)
                .hasAGarage(true)
                .whenLastPurchased(WHEN_LAST_PURCHASED)
                .build();
    }

    private PriorChargesDto getPriorChargesDto() {
        return PriorChargesDto.builder()
                .monthlyPayment(MONTHLY_PAYMENT)
                .balanceOutstanding(OUTSTANDING_BALANCE)
                .otherDebtPayments(OTHER_DEBT_PAYMENTS)
                .balanceConsolidated(BALANCE_CONSOLIDATED)
                .build();
    }

    protected RequiredDto getRequiredDto() {
        return RequiredDto.builder().all(List.of(REQUIRED_PASSPORT)).build();
    }

    protected ChecklistDto getChecklistDto() {
        return ChecklistDto.builder().required(getRequiredDto()).build();
    }

    protected List<ErcDto> getErcDto() {
        return List.of(ErcDto.builder().ercFee(ERC_FEE).period(1).ercAmount(ERC_AMOUNT).ercBalance(ERC_BALANCE).build());
    }

    protected OfferDto getOfferDto() {
        return OfferDto.builder().active(true).id(OFFER_ID).hasFee(true)
                .checklist(getChecklistDto())
                .maxErc(MAX_ERC)
                .ercData(getErcDto())
                .productCode(PRODUCT_CODE).build();
    }

    protected DIPApplicationDto getDIPApplicationDto() {
        return DIPApplicationDto.builder().id(APPLICATION_ID).externalApplicationId(EXTERNAL_APPLICATION_ID).status(DECISIONING_ACCEPT).createdDate(CREATED_DATE).applicants(
                List.of(getDIPApplicantDto())).loanInformation(getAdvancedLoanInformationDto()).propertyDetails(
                getPropertyDetailValueDto()).requestType(DIP_APPLICATION_TYPE).offers(List.of(getOfferDto())).build();
    }

    protected Applicant getApplicant() {
        return Applicant.builder()
                .title(TITLE)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .gender(GENDER)
                .emailAddress(EMAIL_ADDRESS)
                .mobilePhoneNumber(MOBILE_NUMBER)
                .identifier(1)
                .primaryApplicant(true)
                .estimatedRetirementAge(ESTIMATED_RETIREMENT_AGE)
                .addresses(List.of(getAddress()))
                .nationality(NATIONALITY)
                .dateOfBirth(DOB)
                .previousNames(List.of(getPreviousName()))
                .employment(getEmployment())
                .income(getIncomes())
                .checklist(getChecklist())
                .creditCheck(getCreditCheck())
                .documents(List.of(getDocument()))
                .build();
    }

    private PreviousName getPreviousName() {
        return PreviousName.builder().firstName(FIRST_NAME).build();
    }

    private Document getDocument() {
        return Document.builder().documentType(DOCUMENT_TYPE).build();
    }

    private CreditCheck getCreditCheck() {
        return CreditCheck.builder().serviceUsed(CREDIT_CHECK_SERVICE_USED).hardCheckCompleted(false).creditCheckReference(CREDIT_CHECK_REF).creditScore(CREDIT_SCORE).build();
    }

    private Incomes getIncomes() {
        return Incomes.builder().income(List.of(Income.builder().amount(INCOME_AMOUNT).build())).build();
    }

    protected Employment getEmployment() {
        return Employment.builder().employerName(EMPLOYER_NAME).build();
    }

    protected Address getAddress() {
        return Address.builder()
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .addressType(ADDRESS_TYPE)
                .country(COUNTRY)
                .buildingNumber(BUILDING_NUMBER)
                .buildingName(BUILDING_NAME)
                .city(CITY)
                .postcode(POSTCODE)
                .udprn(UDPRN)
                .poBox(PO_BOX)
                .county(COUNTY)
                .from(FROM_DATE)
                .build();
    }

    protected LoanInformation getLoanInformation() {
        return LoanInformation.builder()
                .loanPurpose(LOAN_PURPOSE)
                .requestedLoanAmount(LOAN_AMOUNT)
                .numberOfApplicants(1)
                .requestedLoanTerm(LOAN_TERM)
                .facilities(List.of(getFacility()))
                .desiredTimeLine(DESIRED_TIME_LINE)
                .build();
    }

    protected Facility getFacility() {
        return Facility.builder().allocationAmount(ALLOCATION_AMOUNT).allocationPurpose(ALLOCATION_PURPOSE).build();
    }

    protected Fees getFees() {
        return Fees.builder().arrangementFee(ARRANGEMENT_FEE).isAddProductFeesToFacility(true).build();
    }

    protected Expenditure getExpenditure() {
        return Expenditure.builder().expenditureType(EXPENDITURE_TYPE).build();
    }

    protected PropertyDetails getPropertyDetails() {
        return PropertyDetails.builder()
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .buildingName(BUILDING_NAME)
                .buildingNumber(BUILDING_NUMBER)
                .city(CITY)
                .country(COUNTRY)
                .county(COUNTY)
                .postcode(POSTCODE)
                .estimatedValue(ESTIMATED_VALUE)
                .purchaseValue(PURCHASE_VALUE)
                .whenHasLastPurchased(WHEN_LAST_PURCHASED)
                .propertyType(PROPERTY_TYPE)
                .numberOfBedrooms(NUMBER_OF_BEDROOMS)
                .hasAGarage(true)
                .numberOfPriorCharges(1)
                .priorCharges(getPriorCharges())
                .build();
    }

    private PriorCharges getPriorCharges() {
        return PriorCharges.builder()
                .monthlyPayment(MONTHLY_PAYMENT)
                .balanceOutstanding(OUTSTANDING_BALANCE)
                .otherDebtPayments(OTHER_DEBT_PAYMENTS)
                .balanceConsolidated(BALANCE_CONSOLIDATED)
                .build();
    }

    protected Application getApplication() {
        return Application.builder()
                .id(APPLICATION_ID)
                .decision(ACCEPT_DECISION)
                .createdDate(CREATED_DATE)
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .applicants(List.of(getApplicant()))
                .loanInformation(getLoanInformation())
                .propertyDetails(getPropertyDetails())
                .applicationType(DIP_APPLICATION_TYPE)
                .offers(List.of(getOffer()))
                .build();
    }

    protected Required getRequired() {
        return Required.builder().all(List.of(REQUIRED_PASSPORT)).build();
    }

    protected Checklist getChecklist() {
        return Checklist.builder().required(getRequired()).build();
    }

    protected RuleOutcome getRuleOutcome() {
        return RuleOutcome.builder().outcome(RULE_OUTCOME).build();
    }

    protected List<Erc> getErc() {
        return List.of(Erc.builder().period(1).ercFee(ERC_FEE).ercBalance(ERC_BALANCE).ercAmount(ERC_AMOUNT).build(),
                Erc.builder().period(2).ercFee(ERC_FEE).ercBalance(ERC_BALANCE).ercAmount(ERC_AMOUNT).build());
    }

    protected Offer getOffer() {
        return getOffer(OFFER_DECISION_ACCEPT);
    }

    protected Offer getOffer(String decision) {
        return Offer.builder().active(true).id(OFFER_ID).hasFee(true).productCode(PRODUCT_CODE)
                .checklist(getChecklist())
                .product(PRODUCT_NAME)
                .ruleOutcomes(List.of(getRuleOutcome()))
                .family(HOMEOWNER_LOAN)
                .category(CATEGORY_STATUS_0)
                .ercPeriodYears(2)
                .maximumBalanceEsis(MAX_BALANCE_ESIS)
                .affordabilityDeficit(AFFORDABILITY_DEFICIT)
                .ercShortcode(ERC_SHORT_CODE)
                .maxErc(MAX_ERC)
                .ercData(getErc())
                .productFeeAddedToLoan(true)
                .decision(decision)
                .brokerFeesUpfront(BROKER_FEES_UPFRONT)
                .brokerFeesIncluded(BROKER_FEES_INCLUDED)
                .initialRate(INITIAL_RATE)
                .initialTerm(INITIAL_TERM)
                .initialPayment(INITIAL_PAYMENT)
                .initialMargin(INITIAL_MARGIN)
                .reversionPayment(REVERSION_PAYMENT)
                .reversionMargin(REVERSION_MARGIN)
                .reversionTerm(REVERSION_TERM.doubleValue())
                .isVariable(true)
                .ltvCap(LTV_CAP)
                .svr(SVR)
                .productFee(PRODUCT_FEE)
                .totalAmountRepaid(TOTAL_AMOUNT_REPAID)
                .offerBalance(OFFER_BALANCE)
                .requestedLoanAmount(REQUESTED_LOAN_AMOUNT)
                .maximumLoanAmount(MAX_LOAN_AMOUNT)
                .term(LOAN_TERM)
                .ear(EAR)
                .hasErc(true)
                .build();
    }

    protected ApplicationResponse getApplicationResponse() {
        return ApplicationResponse.builder().applicationType(DIP_APPLICATION_TYPE) .applicationId(APPLICATION_ID).application(getApplication()).creditCommitment(getCreditCommitment()).build();
    }

    protected QuickQuoteCFRequest getQuickQuoteCFRequest() {
        return QuickQuoteCFRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .loanInformation(getLoanInformation())
                .propertyDetails(getPropertyDetails())
                .applicants(List.of(getApplicant()))
                .fees(getFees())
                .build();
    }

    protected QuickQuoteCFResponse getQuickQuoteCFResponse() {
        return QuickQuoteCFResponse.builder()
                .status(DECISION)
                .offers(List.of(getOffer()))
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .build();
    }

    protected ApplicationDecisionResponse getApplicationDecisionResponse() {
        return ApplicationDecisionResponse.builder()
                .id(APPLICATION_ID)
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .applicants(List.of(getApplicant()))
                .applicationType(DIP_APPLICATION_TYPE)
                .loanInformation(getLoanInformation())
                .propertyDetails(getPropertyDetails())
                .offers(List.of(getOffer()))
                .expenditure(List.of(getExpenditure()))
                .fees(getFees())
                .createdDate(CREATED_DATE)
                .modifiedDate(MODIFIED_DATE)
                .underwriting(getUnderwriting())
                .lead(getLead())
                .intermediary(getIntermediary())
                .salesforce(getSalesforce())
                .build();
    }

    protected QuickQuoteEligibilityDecisionResponse getQuickQuoteEligibilityDecisionResponse() {
        return QuickQuoteEligibilityDecisionResponse.builder().decision(DECISION)
                .products(List.of(getProduct()))
                .build();
    }

    protected FilteredQuickQuoteDecisionResponse getFilteredQuickQuoteDecisionResponse() {
        return FilteredQuickQuoteDecisionResponse.builder().decision(DECISION)
                .products(List.of(getProduct()))
                .build();
    }

    protected QuickQuoteResponse getQuickQuoteResponse() {
        return QuickQuoteResponse.builder().build();
    }

    protected Product getProduct() {
        return Product.builder()
                .isVariable(true)
                .family(HOMEOWNER_LOAN)
                .category(CATEGORY_STATUS_0)
                .code(CODE)
                .hasErc(true)
                .ercProfile(ERC_PROFILE)
                .ercShortCode(ERC_SHORT_CODE)
                .name(OFFER_VARIABLE_RATE_50_LTV)
                .variant(OFFER_VARIANT)
                .offer(getProductOffer())
                .build();
    }

    protected ProductOffer getProductOffer() {
        return ProductOffer.builder()
                .id(OFFER_ID)
                .totalAmountRepaid(TOTAL_AMOUNT_REPAID)
                .procFee(PROC_FEE)
                .aprc(APRC)
                .isAprcHeadline(IS_APRC_HEADLINE)
                .ear(EAR)
                .svr(SVR)
                .offerBalance(OFFER_BALANCE)
                .initialPayment(INITIAL_PAYMENT)
                .reversionPayment(REVERSION_PAYMENT)
                .initialRate(INITIAL_RATE)
                .initialTerm(LOAN_TERM)
                .term(LOAN_TERM)
                .lti(LTI)
                .reversionTerm(REVERSION_TERM)
                .brokerFeesIncluded(BROKER_FEES_INCLUDED)
                .maximumLoanAmount(MAX_LOAN_AMOUNT)
                .hasFee(true)
                .decision(OFFER_DECISION_ACCEPT)
                .canAddProductFee(true)
                .productFee(FEE)
                .requestedLoanAmount(REQUESTED_LOAN_AMOUNT)
                .hasProductFeeAddedToLoan(true)
                .ltvCap(LTV_CAP)
                .cltv(CLTV)
                .ercPeriodYears(ERC_PERIOD_YEARS)
                .maxErc(MAX_ERC)
                .brokerFeesUpfront(BROKER_FEES_UPFRONT)
                .affordabilityDeficit(AFFORDABILITY_DEFICIT)
                .maximumBalanceEsis(MAX_BALANCE_ESIS)
                .ercData(getErc())
                .eligibility(ELIGIBILITY)
                .expiryDate(EXPIRY_DATE)
                .dtir(DTIR)
                .bbr(BBR)
                .initialMargin(INITIAL_MARGIN)
                .poundPaidPerBorrowed(POUND_PAID_PER_BORROWED)
                .monthlyPaymentStressed(MONTHLY_PAYMENT_STRESSED)
                .decisionAml(DECISION_AML)
                .decisionFraud(DECISION_FRAUD)
                .baseRateStressed(BASE_RATE_STRESSED)
                .aprcStressed(APRC_STRESSED)
                .propertyValuation(PROPERTY_VALUATION)
                .initialRateMinimum(INITIAL_RATE_MINIMUM)
                .reversionMargin(REVERSION_MARGIN)
                .reversionRateMinimum(REVERSION_RATE_MINIMUM)
                .drawdownTerm(DRAWDOWN_TERM)
                .esisLtvCappedBalance(ESIS_LTV_CAPPED_BALANCE)
                .incomePrimaryApplicant(INCOME_PRIMARY_APPLICANT)
                .incomeJointApplicant(INCOME_JOINT_APPLICANT)
                .daysUntilInitialDrawdown(DAYS_UNTIL_INITIAL_DRAWDOWN)
                .fixedTermYears(FIXED_TERM_YEARS)
                .minimumInitialDrawdown(MINIMUM_INITIAL_DRAWDOWN)
                .offerValidity(OFFER_VALIDITY)
                .esisLoanAmount(ESIS_LOAN_AMOUNT)
                .arrangementFeeSelina(ARRANGEMENT_FEE_SELINA)
                .netLtv(NET_LTV)
                .netLoanAmount(NET_LOAN_AMOUNT)
                .build();
    }

    private Salesforce getSalesforce() {
        return Salesforce.builder().opportunityId(SALESFORCE_OPPORTUNITY_ID).accountId(SALESFORCE_ACCOUNT_ID).build();
    }

    private Intermediary getIntermediary() {
        return Intermediary.builder().contactFirstName(INTERMEDIARY_FIRSTNAME).fcaNumber(FCA_NUMBER).build();
    }

    private Lead getLead() {
        return Lead.builder().utmCampaign(UTM_CAMPAIGN).utmMedium(UTM_MEDIUM).utmSource(UTM_SOURCE).build();
    }

    public CreditCommitmentResponse getCreditCommitmentResponse() {
        return CreditCommitmentResponse.builder().creditCommitment(getCreditCommitment()).build();
    }

    private CreditCommitment getCreditCommitment() {
        return CreditCommitment.builder().applicants(List.of(getApplicantCreditCommitment())).build();
    }

    private ApplicantCreditCommitments getApplicantCreditCommitment() {
        return ApplicantCreditCommitments.builder()
                .votersRoll(getVotersRoll())
                .creditPolicy(getCreditPolicy())
                .publicInformation(getPublicInformation())
                .creditCommitments(getCreditCommitments()).primaryApplicant(true).creditScore(CREDIT_SCORE).build();
    }

    protected CreditCommitmentsDetail getCreditCommitments() {
        return CreditCommitmentsDetail.builder()
                .system(getSystem())
                .user(getUser())
                .build();
    }

    protected Partner getPartner() {
        return Partner.builder()
                .subUnitId(SUB_UNIT_ID)
                .companyId(COMPANY_ID)
                .build();
    }

    protected EligibilityResponse getEligibilityResponse() {
        return EligibilityResponse.builder()
                .eligibility(ELIGIBILITY)
                .propertyInfo(getPropertyInfo())
                .build();
    }

    private PropertyInfo getPropertyInfo() {
        return PropertyInfo.builder()
                .estimatedValue(ELIGIBILITY_ESTIMATED_VALUE)
                .build();
    }

    private User getUser() {
        return User.builder().detail(List.of(getDetail())).build();
    }

    private PublicInformation getPublicInformation() {
        return PublicInformation.builder().user(getUser()).system(getSystem()).build();
    }

    private VotersRoll getVotersRoll() {
        return VotersRoll.builder().detail(List.of(getDetail())).build();
    }

    private CreditPolicy getCreditPolicy() {
        return CreditPolicy.builder().detail(List.of(getDetail())).build();
    }

    private Detail getDetail() {
        return Detail.builder().id(DETAIL_ID).status(STATUS).build();
    }

    private System getSystem() {
        return System.builder().detail(List.of(getDetail())).summary(getSummary()).build();
    }


    private Summary getSummary() {
        return Summary.builder().numberAccounts(2).outstandingBalance(OUTSTANDING_BALANCE).build();
    }

    private Underwriting getUnderwriting() {
        return Underwriting.builder().stageName(UNDERWRITING_STAGE).build();
    }
}
