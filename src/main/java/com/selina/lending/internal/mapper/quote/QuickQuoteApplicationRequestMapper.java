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

package com.selina.lending.internal.mapper.quote;

import java.time.LocalDate;
import java.util.List;

import com.selina.lending.internal.dto.IncomeDto;
import com.selina.lending.internal.dto.IncomeItemDto;
import com.selina.lending.internal.dto.LoanInformationDto;
import com.selina.lending.internal.dto.PriorChargesDto;
import com.selina.lending.internal.dto.quote.QuickQuoteApplicantDto;
import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.dto.quote.QuickQuotePropertyDetailsDto;
import com.selina.lending.internal.service.application.domain.quote.Applicant;
import com.selina.lending.internal.service.application.domain.quote.Application;
import com.selina.lending.internal.service.application.domain.quote.FilterQuickQuoteApplicationRequest;
import com.selina.lending.internal.service.application.domain.quote.Income;
import com.selina.lending.internal.service.application.domain.quote.LoanInformation;
import com.selina.lending.internal.service.application.domain.quote.Options;
import com.selina.lending.internal.service.application.domain.quote.PriorCharges;
import com.selina.lending.internal.service.application.domain.quote.PropertyDetails;

public class QuickQuoteApplicationRequestMapper {

    private QuickQuoteApplicationRequestMapper() {}

    public static FilterQuickQuoteApplicationRequest mapRequest(QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        return FilterQuickQuoteApplicationRequest.builder()
                .application(mapApplication(quickQuoteApplicationRequest))
                .options(mapOptions(quickQuoteApplicationRequest))
                .build();
    }

    private static Options mapOptions(QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        return Options.builder()
                .priorCharges(mapPriorCharges(quickQuoteApplicationRequest.getPropertyDetails()))
                .build();
    }

    private static PriorCharges mapPriorCharges(QuickQuotePropertyDetailsDto propertyDetailsDto) {
        if (propertyDetailsDto == null) {
            return null;
        }
        PriorChargesDto priorChargesDto = propertyDetailsDto.getPriorCharges();
        return PriorCharges.builder()
                .numberPriorCharges(propertyDetailsDto.getNumberOfPriorCharges())
                .monthlyPayment(priorChargesDto.getMonthlyPayment())
                .balanceOutstanding(priorChargesDto.getOutstandingBalance())
                .build();
    }

    private static Application mapApplication(QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        return Application.builder()
                .externalApplicationId(quickQuoteApplicationRequest.getExternalApplicationId())
                .applicants(mapApplicants(quickQuoteApplicationRequest.getApplicants()))
                .loanInformation(mapLoanInformation(quickQuoteApplicationRequest.getLoanInformation()))
                .propertyDetails(mapPropertyDetails(quickQuoteApplicationRequest.getPropertyDetails()))
                .build();
    }

    private static PropertyDetails mapPropertyDetails(QuickQuotePropertyDetailsDto propertyDetailsDto) {
        return propertyDetailsDto == null ? null : PropertyDetails.builder().estimatedValue(
                propertyDetailsDto.getEstimatedValue()).postcode(propertyDetailsDto.getPostcode()).build();
    }

    private static LoanInformation mapLoanInformation(LoanInformationDto loanInformationDto) {
        return loanInformationDto == null ? null : LoanInformation.builder().numberOfApplicants(
                loanInformationDto.getNumberOfApplicants()).requestedLoanAmount(
                Double.valueOf(loanInformationDto.getRequestedLoanAmount())).requestedLoanTerm(
                loanInformationDto.getRequestedLoanTerm()).build();
    }

    private static List<Applicant> mapApplicants(List<QuickQuoteApplicantDto> applicantDto) {
        return applicantDto.stream().map(QuickQuoteApplicationRequestMapper::mapApplicant).toList();
    }

    private static Applicant mapApplicant(QuickQuoteApplicantDto applicantDto) {
        return Applicant.builder()
                .firstName(applicantDto.getFirstName())
                .lastName(applicantDto.getLastName())
                .middleName(applicantDto.getMiddleName())
                .numberOfAdultDependants(applicantDto.getNumberOfAdultDependants())
                .numberOfChildDependants(applicantDto.getNumberOfChildDependants())
                .dateOfBirth(LocalDate.parse(applicantDto.getDateOfBirth()))
                .incomes(mapIncomes(applicantDto.getIncome()))
                .build();
    }

    private static List<Income> mapIncomes(IncomeDto incomeDto) {
        return incomeDto.getIncome().stream().map(QuickQuoteApplicationRequestMapper::mapIncome).toList();
    }

    private static Income mapIncome(IncomeItemDto incomeItemDto) {
        return Income.builder().amount(incomeItemDto.getAmount()).type(incomeItemDto.getType()).build();
    }
}
