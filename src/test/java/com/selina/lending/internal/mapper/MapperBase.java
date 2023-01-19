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

package com.selina.lending.internal.mapper;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.selina.lending.internal.dto.AddressDto;
import com.selina.lending.internal.dto.AdvancedLoanInformationDto;
import com.selina.lending.internal.dto.ApplicantDto;
import com.selina.lending.internal.dto.ApplicationRequest;
import com.selina.lending.internal.dto.ChecklistDto;
import com.selina.lending.internal.dto.DIPApplicantDto;
import com.selina.lending.internal.dto.DIPApplicationDto;
import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.dto.DIPCCApplicationRequest;
import com.selina.lending.internal.dto.DIPCCPropertyDetailsDto;
import com.selina.lending.internal.dto.DIPPropertyDetailsDto;
import com.selina.lending.internal.dto.EmploymentDto;
import com.selina.lending.internal.dto.ErcDto;
import com.selina.lending.internal.dto.ExpenditureDto;
import com.selina.lending.internal.dto.FacilityDto;
import com.selina.lending.internal.dto.FeesDto;
import com.selina.lending.internal.dto.IncomeDto;
import com.selina.lending.internal.dto.IncomeItemDto;
import com.selina.lending.internal.dto.LoanInformationDto;
import com.selina.lending.internal.dto.OfferDto;
import com.selina.lending.internal.dto.PreviousNameDto;
import com.selina.lending.internal.dto.PriorChargesDto;
import com.selina.lending.internal.dto.PropertyDetailValueDto;
import com.selina.lending.internal.dto.PropertyDetailsDto;
import com.selina.lending.internal.dto.RequiredDto;
import com.selina.lending.internal.dto.creditcommitments.request.ApplicantCreditCommitmentsDto;
import com.selina.lending.internal.dto.creditcommitments.request.CreditCommitmentsDetailDto;
import com.selina.lending.internal.dto.creditcommitments.request.DetailDto;
import com.selina.lending.internal.dto.creditcommitments.request.PublicInformationDto;
import com.selina.lending.internal.dto.creditcommitments.request.SystemDto;
import com.selina.lending.internal.dto.creditcommitments.request.UpdateCreditCommitmentsRequest;
import com.selina.lending.internal.dto.creditcommitments.request.UserDto;
import com.selina.lending.internal.dto.quote.QuickQuoteApplicantDto;
import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.dto.quote.QuickQuotePropertyDetailsDto;
import com.selina.lending.internal.service.application.domain.Address;
import com.selina.lending.internal.service.application.domain.Applicant;
import com.selina.lending.internal.service.application.domain.ApplicantCreditCommitments;
import com.selina.lending.internal.service.application.domain.Application;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import com.selina.lending.internal.service.application.domain.Checklist;
import com.selina.lending.internal.service.application.domain.CreditCheck;
import com.selina.lending.internal.service.application.domain.CreditCommitment;
import com.selina.lending.internal.service.application.domain.CreditCommitmentsDetail;
import com.selina.lending.internal.service.application.domain.CreditPolicy;
import com.selina.lending.internal.service.application.domain.Detail;
import com.selina.lending.internal.service.application.domain.Document;
import com.selina.lending.internal.service.application.domain.Employment;
import com.selina.lending.internal.service.application.domain.Erc;
import com.selina.lending.internal.service.application.domain.Expenditure;
import com.selina.lending.internal.service.application.domain.Facility;
import com.selina.lending.internal.service.application.domain.Fees;
import com.selina.lending.internal.service.application.domain.Income;
import com.selina.lending.internal.service.application.domain.Incomes;
import com.selina.lending.internal.service.application.domain.Intermediary;
import com.selina.lending.internal.service.application.domain.Lead;
import com.selina.lending.internal.service.application.domain.LoanInformation;
import com.selina.lending.internal.service.application.domain.Offer;
import com.selina.lending.internal.service.application.domain.PreviousName;
import com.selina.lending.internal.service.application.domain.PriorCharges;
import com.selina.lending.internal.service.application.domain.PropertyDetails;
import com.selina.lending.internal.service.application.domain.PublicInformation;
import com.selina.lending.internal.service.application.domain.Required;
import com.selina.lending.internal.service.application.domain.RuleOutcome;
import com.selina.lending.internal.service.application.domain.Salesforce;
import com.selina.lending.internal.service.application.domain.Summary;
import com.selina.lending.internal.service.application.domain.System;
import com.selina.lending.internal.service.application.domain.Underwriting;
import com.selina.lending.internal.service.application.domain.User;
import com.selina.lending.internal.service.application.domain.VotersRoll;
import com.selina.lending.internal.service.application.domain.creditcommitments.CreditCommitmentResponse;
import com.selina.lending.internal.service.application.domain.quote.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.internal.service.application.domain.quote.Product;
import com.selina.lending.internal.service.application.domain.quote.ProductOffer;

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
    public static final String INCOME_TYPE = "Base salary";
    public static final String LOAN_PURPOSE = "Home improvements";
    public static final Integer LOAN_AMOUNT = 50000;
    public static final int LOAN_TERM = 5;
    public static final double ALLOCATION_AMOUNT = 50000;
    public static final Double AFFORDABILITY_DEFICIT = 2100.0;
    public static final String ALLOCATION_PURPOSE = "Home improvements";
    public static final String DESIRED_TIME_LINE = "By 3 months";
    public static final Double ESTIMATED_VALUE = 590000.00;
    public static final String WHEN_LAST_PURCHASED = "1990-01-05";
    public static final Double PURCHASE_VALUE = 390000.00;
    public static final String PROPERTY_TYPE = "Detached house";
    public static final int NUMBER_OF_BEDROOMS = 4;
    public static final String DIP_APPLICATION_TYPE = "DIP";
    public static final String APPLICATION_ID = "123456789";
    public static final Date CREATED_DATE = Date.from(Instant.now());
    public static final String ADDRESS_LINE_1 = "address line 1";
    public static final String ADDRESS_LINE_2 = "address line 2";
    public static final String ADDRESS_TYPE = "current";
    public static final String COUNTRY = "England";
    public static final String BUILDING_NUMBER = "10";
    public static final String CITY = "a city";
    public static final String POSTCODE = "postcode";
    public static final int UDPRN = 1235;
    public static final String PO_BOX = "poBox";
    public static final String BUILDING_NAME = "building name";
    public static final String COUNTY = "county";
    public static final String FROM_DATE = "2000-01-21";
    public static final String OFFER_ID = "offer123";
    public static final String PRODUCT_CODE = "All";
    public static final String EXPENDITURE_TYPE = "Utilities";
    protected static final Double ARRANGEMENT_FEE = 1000.00;
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
    public static final String UNDERWRITER = "Madeline Scott";
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
    public static final String HOMEOWNER_LOAN = "Homeowner";
    public static final String DECISION = "Accepted";
    public static final String OFFER_VARIABLE_RATE_50_LTV = "Variable Rate - 50% LTV";
    public static final Double TOTAL_AMOUNT_REPAID = 60352.20;
    public static final Double INITIAL_RATE = 8.75;
    public static final Double INITIAL_PAYMENT = 411.08;
    public static final Double APRC = 9.77;
    public static final Double REQUESTED_LOAN_AMOUNT = 50000.0;
    public static final Double OUTSTANDING_BALANCE = 20000.0;
    public static final Double BALANCE_CONSOLIDATED = 25000.0;
    public static final Double OTHER_DEBT_PAYMENTS = 5000.0;
    public static final Double MAX_BALANCE_ESIS = 100000.0;
    public static final String ERC_SHORT_CODE = "ERC_01";
    public static final String CODE = "HOL00750";
    public static final Double EAR = 9.39;
    public static final Double SVR = 9.0;
    public static final Double OFFER_BALANCE = 30000.0;
    public static final String DECISIONING_ACCEPT = "Decisioning Accept";
    public static final Integer REVERSION_TERM = 3;
    public static final Double MAX_LOAN_AMOUNT = 50000.0;

    protected ApplicationRequest getApplicationRequestDto() {
        return ApplicationRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .expenditure(List.of(getExpenditureDto()))
                .build();
    }

    protected DIPApplicationRequest getDIPApplicationRequestDto() {
        return DIPApplicationRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .expenditure(List.of(getExpenditureDto()))
                .applicants(List.of(getDIPApplicantDto()))
                .loanInformation(getAdvancedLoanInformationDto())
                .propertyDetails(getDIPPropertyDetailsDto())
                .fees(getFeesDto())
                .build();
    }


    protected DIPCCApplicationRequest getDIPCCApplicationRequestDto() {
        return DIPCCApplicationRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .expenditure(List.of(getExpenditureDto()))
                .applicants(List.of(getDIPApplicantDto()))
                .loanInformation(getAdvancedLoanInformationDto())
                .propertyDetails(getDIPCCPropertyDetailsDto())
                .fees(getFeesDto())
                .build();
    }

    protected QuickQuoteApplicationRequest getQuickQuoteApplicationRequestDto() {
        return QuickQuoteApplicationRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .expenditure(List.of(getExpenditureDto()))
                .loanInformation(getLoanInformationDto())
                .propertyDetails(getQuickQuotePropertyDetailsDto())
                .applicants(List.of(getQuickQuoteApplicantDto()))
                .build();
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

    protected ExpenditureDto getExpenditureDto() {
        return ExpenditureDto.builder().expenditureType(EXPENDITURE_TYPE).build();
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
                .fromDate(FROM_DATE)
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

    protected EmploymentDto getEmploymentDto() {
        return EmploymentDto.builder().employmentStatus(EMPLOYED_STATUS).employerName(EMPLOYER_NAME)
                .inProbationPeriod(false)
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
        return QuickQuotePropertyDetailsDto.builder().addressLine1(ADDRESS_LINE_1).addressLine2(ADDRESS_LINE_2).buildingName(
                BUILDING_NAME).buildingNumber(BUILDING_NUMBER).city(CITY).country(COUNTRY).county(COUNTY).postcode(
                POSTCODE).estimatedValue(ESTIMATED_VALUE).whenLastPurchased(WHEN_LAST_PURCHASED)
                .purchaseValue(PURCHASE_VALUE).numberOfPriorCharges(1).priorCharges(getPriorChargesDto()).build();
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
        return Offer.builder().active(true).id(OFFER_ID).hasFee(true).productCode(PRODUCT_CODE)
                .checklist(getChecklist())
                .ruleOutcomes(List.of(getRuleOutcome()))
                .family(HOMEOWNER_LOAN)
                .ercPeriodYears(2)
                .maximumBalanceEsis(MAX_BALANCE_ESIS)
                .affordabilityDeficit(AFFORDABILITY_DEFICIT)
                .ercShortCode(ERC_SHORT_CODE)
                .maxErc(MAX_ERC)
                .ercData(getErc())
                .build();
    }

    protected ApplicationResponse getApplicationResponse(){
        return ApplicationResponse.builder().applicationType(DIP_APPLICATION_TYPE).applicationId(APPLICATION_ID).application(getApplication()).creditCommitment(getCreditCommitment()).build();
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
                .underwriting(getUnderwriting())
                .lead(getLead())
                .intermediary(getIntermediary())
                .salesforce(getSalesforce())
                .build();
    }

    protected FilteredQuickQuoteDecisionResponse getFilteredQuickQuoteDecisionResponse() {
        return FilteredQuickQuoteDecisionResponse.builder().decision(DECISION)
                .products(List.of(Product.builder()
                        .isVariable(true)
                        .family(HOMEOWNER_LOAN)
                        .code(CODE)
                        .hasErc(true)
                        .ercShortCode(ERC_SHORT_CODE)
                        .name(OFFER_VARIABLE_RATE_50_LTV)
                        .offer(getProductOffer())
                        .build()))
                .build();
    }

    private ProductOffer getProductOffer() {
        return ProductOffer.builder()
                .id(OFFER_ID)
                .totalAmountRepaid(TOTAL_AMOUNT_REPAID)
                .aprc(APRC)
                .ear(EAR)
                .svr(SVR)
                .offerBalance(OFFER_BALANCE)
                .initialPayment(INITIAL_PAYMENT)
                .initialRate(INITIAL_RATE)
                .initialTerm(LOAN_TERM)
                .term(LOAN_TERM)
                .reversionTerm(REVERSION_TERM)
                .maximumLoanAmount(MAX_LOAN_AMOUNT)
                .hasFee(true)
                .productFee(FEE)
                .requestedLoanAmount(REQUESTED_LOAN_AMOUNT)
                .hasProductFeeAddedToLoan(true)
                .ercPeriodYears(2)
                .maxErc(MAX_ERC)
                .ercData(getErc())
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

    public CreditCommitmentsDetail getCreditCommitments() {
        return CreditCommitmentsDetail.builder()
                .system(getSystem())
                .user(getUser())
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
        return Underwriting.builder().underwritingOwner(UNDERWRITER).stageName(UNDERWRITING_STAGE).build();
    }
}
