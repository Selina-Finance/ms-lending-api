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

package com.selina.lending.internal.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.selina.lending.internal.dto.DIPPropertyDetailsDto;
import com.selina.lending.internal.dto.EmploymentDto;
import com.selina.lending.internal.dto.ExpenditureDto;
import com.selina.lending.internal.dto.FacilityDto;
import com.selina.lending.internal.dto.FeesDto;
import com.selina.lending.internal.dto.IncomeDto;
import com.selina.lending.internal.dto.IncomeItemDto;
import com.selina.lending.internal.dto.LoanInformationDto;
import com.selina.lending.internal.dto.OfferDto;
import com.selina.lending.internal.dto.PropertyDetailsDto;
import com.selina.lending.internal.dto.RequiredDto;
import com.selina.lending.internal.service.application.domain.Address;
import com.selina.lending.internal.service.application.domain.Applicant;
import com.selina.lending.internal.service.application.domain.Application;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import com.selina.lending.internal.service.application.domain.Checklist;
import com.selina.lending.internal.service.application.domain.Employment;
import com.selina.lending.internal.service.application.domain.Expenditure;
import com.selina.lending.internal.service.application.domain.Facility;
import com.selina.lending.internal.service.application.domain.Fees;
import com.selina.lending.internal.service.application.domain.Income;
import com.selina.lending.internal.service.application.domain.Incomes;
import com.selina.lending.internal.service.application.domain.LoanInformation;
import com.selina.lending.internal.service.application.domain.Offer;
import com.selina.lending.internal.service.application.domain.PropertyDetails;
import com.selina.lending.internal.service.application.domain.Required;
import com.selina.lending.internal.service.application.domain.RuleOutcome;

public abstract class MapperBase {
    public static final String TITLE = "Mrs";
    public static final String FIRST_NAME = "Sally";
    public static final String LAST_NAME = "Smith";
    public static final String EMAIL_ADDRESS = "sally.smith@someemail.com";
    public static final String GENDER = "Female";
    public static final String MOBILE_NUMBER = "07965234654";
    public static final Integer ESTIMATED_RETIREMENT_AGE = 65;
    public static final String NATIONALITY = "British";
    public static final Date DOB;
    public static final String EMPLOYER_NAME = "Employer name";
    public static final Double INCOME_AMOUNT = 15000.00;
    public static final String INCOME_TYPE = "Gross Salary";
    public static final String LOAN_PURPOSE = "Home improvements";
    public static final Integer LOAN_AMOUNT = 50000;
    public static final int LOAN_TERM = 5;
    public static final double ALLOCATION_AMOUNT = 50000;
    public static final String ALLOCATION_PURPOSE = "Home improvements";
    public static final String DESIRED_TIME_LINE = "By 3 months";
    public static final Double ESTIMATED_VALUE = 590000.00;
    public static final String WHEN_LAST_PURCHASED = "1990";
    public static final Double PURCHASE_VALUE = 390000.00;
    public static final String PROPERTY_TYPE = "House";
    public static final int NUMBER_OF_BEDROOMS = 4;
    public static final String DIP_APPLICATION_TYPE = "DIP";
    public static final String APPLICATION_TYPE = "Quick Quote";
    public static final String APPLICATION_ID = "123456789";
    public static final Date CREATED_DATE = Date.from(Instant.now());
    public static final String ADDRESS_LINE_1 = "address line 1";
    public static final String ADDRESS_LINE_2 = "address line 2";
    public static final String ADDRESS_TYPE = "Home";
    public static final String COUNTRY = "England";
    public static final String BUILDING_NUMBER = "10";
    public static final String CITY = "a city";
    public static final String POSTCODE = "postcode";
    public static final int UDPRN = 1235;
    public static final String PO_BOX = "poBox";
    public static final String BUILDING_NAME = "building name";
    public static final String COUNTY = "county";
    public static final Date FROM_DATE;
    public static final String OFFER_ID = "offer123";
    public static final String PRODUCT_CODE = "product123";
    public static final String EXPENDITURE_TYPE = "expenditure type";
    public static final String SOURCE = "broker source";
    protected static final Double ARRANGEMENT_FEE = 1000.00;
    public static final String EXTERNAL_APPLICATION_ID = "uniqueCaseID";
    public static final String RULE_OUTCOME = "Granted";
    public static final String REQUIRED_PASSPORT = "Passport";

    static {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DOB = simpleDateFormat.parse("1975-03-12");
            FROM_DATE = simpleDateFormat.parse("2000-01-21");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<AddressDto> getAddressDtoList() {
        return List.of(getAddressDto());
    }

    protected ApplicationRequest getApplicationRequestDto() {
        return ApplicationRequest.builder()
                .requestType(APPLICATION_TYPE)
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .expenditure(List.of(getExpenditureDto()))
                .productCode(PRODUCT_CODE)
                .source(SOURCE)
                .build();
    }

    protected DIPApplicationRequest getDIPApplicationRequestDto() {
        return DIPApplicationRequest.builder()
                .requestType(DIP_APPLICATION_TYPE)
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .expenditure(List.of(getExpenditureDto()))
                .applicants(List.of(getDIPApplicantDto()))
                .loanInformation(getAdvancedLoanInformationDto())
                .propertyDetails(getDIPPropertyDetailsDto())
                .fees(getFeesDto())
                .productCode(PRODUCT_CODE)
                .source(SOURCE)
                .build();
    }

    protected FeesDto getFeesDto() {
        return FeesDto.builder().arrangementFee(ARRANGEMENT_FEE).addProductFeesToFacility(true).build();
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
                .addresses(getAddressDtoList())
                .applicant2LivesWithApplicant1(false)
                .dateOfBirth(DOB)
                .build();
    }

    protected EmploymentDto getEmploymentDto() {
        return EmploymentDto.builder().employerName(EMPLOYER_NAME).build();
    }

    protected DIPApplicantDto getDIPApplicantDto() {
        return DIPApplicantDto.builder()
                .title(TITLE)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .gender(GENDER)
                .emailAddress(EMAIL_ADDRESS)
                .mobileNumber(MOBILE_NUMBER)
                .applicantUsedAnotherName(false)
                .identifier(1)
                .estimatedRetirementAge(ESTIMATED_RETIREMENT_AGE)
                .addresses(getAddressDtoList())
                .nationality(NATIONALITY)
                .applicant2LivesWithApplicant1(false)
                .dateOfBirth(DOB)
                .livedInCurrentAddressFor3Years(true)
                .employment(getEmploymentDto())
                .income(getIncomeDto())
                .build();
    }

    protected IncomeDto getIncomeDto() {
        return IncomeDto.builder().income(List.of(getIncomeItemDto())).build();
    }

    protected IncomeItemDto getIncomeItemDto() {
        return IncomeItemDto.builder().amount(INCOME_AMOUNT).type(INCOME_TYPE).build();
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

    protected PropertyDetailsDto getPropertyDetailsDto() {
        return PropertyDetailsDto.builder().addressLine1(ADDRESS_LINE_1).addressLine2(ADDRESS_LINE_2).buildingName(
                BUILDING_NAME).buildingNumber(BUILDING_NUMBER).city(CITY).country(COUNTRY).county(COUNTY).postcode(
                POSTCODE).estimatedValue(ESTIMATED_VALUE).purchaseValue(PURCHASE_VALUE).build();
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
                .build();
    }

    protected RequiredDto getRequiredDto() {
        return RequiredDto.builder().all(List.of(REQUIRED_PASSPORT)).build();
    }

    protected ChecklistDto getChecklistDto() {
        return ChecklistDto.builder().required(getRequiredDto()).build();
    }

    protected OfferDto getOfferDto() {
        return OfferDto.builder().active(true).id(OFFER_ID).hasFee(true)
                .checklist(getChecklistDto())
                .productCode(PRODUCT_CODE).build();
    }

    protected DIPApplicationDto getDIPApplicationDto() {
        return DIPApplicationDto.builder().id(APPLICATION_ID).externalApplicationId(EXTERNAL_APPLICATION_ID).createdDate(CREATED_DATE).applicants(
                List.of(getDIPApplicantDto())).loanInformation(getAdvancedLoanInformationDto()).propertyDetails(
                getDIPPropertyDetailsDto()).requestType(DIP_APPLICATION_TYPE).offers(List.of(getOfferDto())).build();
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
                .estimatedRetirementAge(ESTIMATED_RETIREMENT_AGE)
                .addresses(getAddressList())
                .nationality(NATIONALITY)
                .applicant2LivesWithApplicant1(false)
                .livedInCurrentAddressFor3Years(true)
                .dateOfBirth(DOB)
                .employment(getEmployment())
                .income(getIncomes())
                .build();
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

    protected List<Address> getAddressList() {
        return List.of(getAddress());
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
        return Fees.builder().arrangementFee(ARRANGEMENT_FEE).addProductFeesToFacility(true).build();
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

    protected Offer getOffer() {
        return Offer.builder().active(true).id(OFFER_ID).hasFee(true).productCode(PRODUCT_CODE)
                .checklist(getChecklist())
                .ruleOutcomes(List.of(getRuleOutcome())).build();
    }

    protected ApplicationResponse getApplicationResponse(){
        return ApplicationResponse.builder().applicationType(DIP_APPLICATION_TYPE).applicationId(APPLICATION_ID).application(getApplication()).build();
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
                .build();
    }
}
