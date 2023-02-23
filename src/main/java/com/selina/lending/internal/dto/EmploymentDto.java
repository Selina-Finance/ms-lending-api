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

package com.selina.lending.internal.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.selina.lending.api.controller.SwaggerConstants;
import com.selina.lending.api.validator.EnumValue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class EmploymentDto {

    @NotBlank
    @Schema(implementation = EmploymentStatus.class, description = "If employment status value is not in the enum list, use best match e.g. Student map to 'Not in paid employment'")
    @EnumValue(enumClass = EmploymentStatus.class)
    String employmentStatus;
    Boolean inProbationPeriod;
    @Schema(implementation = EmploymentType.class, description = "type of employment")
    @EnumValue(enumClass = EmploymentType.class)
    String employmentType;
    String employerPhoneNumber;
    String postcode;
    String addressLine1;
    String addressLine2;
    String city;

    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE, description = "when did the employment start")
    String contractStartDate;

    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE, description = "when did the employment end")
    String contractEndDate;
    Boolean firstTimeContractor;
    String employerName;
    String jobTitle;
    String occupationType;
    Boolean isEmployedByFamilyMember;
    Boolean ownSharesInThisCompany;
    Double shareHolding;
    String registeredCompanyName;
    @Schema(implementation = SelfEmployed.class, description = "is the applicant self employed")
    @EnumValue(enumClass = SelfEmployed.class)
    String selfEmployed;
    String fiscalYearReportedIncomeRelatesTo;
    @Schema(implementation = SelfEmployedLength.class, description = "what is the length the applicant has been self employed")
    @EnumValue(enumClass = SelfEmployedLength.class)
    String lengthSelfEmployed;
    String companyRegistrationNumber;
    String percentageOfCompanyOwned;
    String monthAccountingPeriodStarts;
    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE)
    String whenWasCompanyIncorporated;
    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE)
    String whenDidYouBeginTrading;
    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE)
    String partnershipFormedDate;
    Double percentageOfPartnershipOwned;
    String businessStructure;
    @Schema(implementation = Industry.class, description = "the applicant company industry")
    @EnumValue(enumClass = Industry.class)
    String industry;

    enum EmploymentStatus {
        EMPLOYED("Employed"),
        SELF_EMPLOYED_LIMITED_COMPANY("Self-employed (limited company)"),
        SELF_EMPLOYED_SOLE_TRADER_PARTNERSHIP("Self-employed (sole trader / partnership)"),
        SELF_EMPLOYED_CONTRACTOR("Self-employed (contractor)"),
        RETIRED("Retired"),
        NOT_IN_PAID_EMPLOYMENT("Not in paid employment");

        final String value;
        EmploymentStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    enum EmploymentType {
        PERMANENT_FULL_TIME("Permanent (full-time)"),
        PERMANENT("Permanent (part-time)"),
        FIXED_TERM_FULL_TIME("Fixed-term (full-time)"),
        FIXED_TERM_PART_TIME("Fixed-term (part-time)");

        final String value;

        EmploymentType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    enum SelfEmployed {
        LIMITED_COMPANY("Limited company"),
        SOLE_TRADER("Sole trader"),
        PARTNERSHIP("Partnership"),
        LIMITED_LIABILITY_PARTNERSHIP("Limited liability partnership");

        final String value;

        SelfEmployed(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    enum SelfEmployedLength {
        LESS_THAN_YEAR("Less than a year"),
        BETWEEN_ONE_AND_TWO_YEARS("Between 1 and 2 years"),
        MORE_THAN_TWO_YEARS("More than 2 years");
        final String value;
        SelfEmployedLength(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    enum Industry {
        AGRICULTURE("Agriculture"),
        APPAREL("Apparel"),
        BANKING("Banking"),
        BIOTECHNOLOGY("Biotechnology"),
        CHEMICALS("Chemicals"),
        COMMUNICATIONS("Communications"),
        CONSTRUCTION("Construction"),
        CONSULTING("Consulting"),
        EDUCATION("Education"),
        ELECTRONICS("Electronics"),
        ENERGY("Energy"),
        ENGINEERING("Engineering"),
        ENTERTAINMENT("Entertainment"),
        ENVIRONMENTAL("Environmental"),
        FINANCE("Finance"),
        FOOD_BEVERAGE("Food & Beverage"),
        GOVERNMENT("Government"),
        HEALTHCARE("Healthcare"),
        HOSPITALITY("Hospitality"),
        INSURANCE("Insurance"),
        MACHINERY("Machinery"),
        MANUFACTURING("Manufacturing"),
        MEDIA("Media"),
        NOT_FOR_PROFIT("Not For Profit"),
        OTHER("Other"),
        RECREATION("Recreation"),
        RETAIL("Retail"),
        SHIPPING("Shipping"),
        TECHNOLOGY("Technology"),
        TELECOMMUNICATIONS("Telecommunications"),
        TRANSPORTATION("Transportation"),
        UTILITIES("Utilities");

        final String value;

        Industry(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
