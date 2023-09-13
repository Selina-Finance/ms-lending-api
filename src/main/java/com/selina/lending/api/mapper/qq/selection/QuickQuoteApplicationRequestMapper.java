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

package com.selina.lending.api.mapper.qq.selection;

import com.selina.lending.api.dto.common.IncomeDto;
import com.selina.lending.api.dto.common.IncomeItemDto;
import com.selina.lending.api.dto.common.PriorChargesDto;
import com.selina.lending.api.dto.qq.request.LoanInformationDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.dto.qq.request.QuickQuoteFeesDto;
import com.selina.lending.api.dto.qq.request.QuickQuotePropertyDetailsDto;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import com.selina.lending.httpclient.selection.dto.request.Applicant;
import com.selina.lending.httpclient.selection.dto.request.Application;
import com.selina.lending.httpclient.selection.dto.request.FilterQuickQuoteApplicationRequest;
import com.selina.lending.httpclient.selection.dto.request.Income;
import com.selina.lending.httpclient.selection.dto.request.LoanInformation;
import com.selina.lending.httpclient.selection.dto.request.Options;
import com.selina.lending.httpclient.selection.dto.request.PriorCharges;
import com.selina.lending.httpclient.selection.dto.request.PropertyDetails;

import java.time.LocalDate;
import java.util.List;

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
        PriorCharges priorCharges = null;
        if (propertyDetailsDto != null && propertyDetailsDto.getPriorCharges() != null) {
            PriorChargesDto priorChargesDto = propertyDetailsDto.getPriorCharges();
            priorCharges = PriorCharges.builder()
                    .numberPriorCharges(propertyDetailsDto.getNumberOfPriorCharges())
                    .monthlyPayment(priorChargesDto.getMonthlyPayment())
                    .balanceOutstanding(priorChargesDto.getBalanceOutstanding())
                    .balanceConsolidated(priorChargesDto.getBalanceConsolidated())
                    .otherDebtPayments(priorChargesDto.getOtherDebtPayments())
                    .build();
        }
        return priorCharges;
    }

    private static Application mapApplication(QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        return Application.builder()
                .externalApplicationId(quickQuoteApplicationRequest.getExternalApplicationId())
                .applicants(mapApplicants(quickQuoteApplicationRequest.getApplicants()))
                .loanInformation(mapLoanInformation(quickQuoteApplicationRequest.getLoanInformation()))
                .propertyDetails(mapPropertyDetails(quickQuoteApplicationRequest.getPropertyDetails()))
                .fees(mapFees(quickQuoteApplicationRequest.getFees()))
                .build();
    }

    private static Fees mapFees(QuickQuoteFeesDto quickQuoteFeesDto) {
        return quickQuoteFeesDto == null ? null : Fees.builder()
                .isAddAdviceFeeToLoan(quickQuoteFeesDto.getIsAddAdviceFeeToLoan())
                .isAddArrangementFeeToLoan(quickQuoteFeesDto.getIsAddArrangementFeeToLoan())
                .isAddCommissionFeeToLoan(quickQuoteFeesDto.getIsAddCommissionFeeToLoan())
                .isAddThirdPartyFeeToLoan(quickQuoteFeesDto.getIsAddThirdPartyFeeToLoan())
                .isAddValuationFeeToLoan(quickQuoteFeesDto.getIsAddValuationFeeToLoan())
                .adviceFee(quickQuoteFeesDto.getAdviceFee())
                .arrangementFee(quickQuoteFeesDto.getArrangementFee())
                .commissionFee(quickQuoteFeesDto.getCommissionFee())
                .thirdPartyFee(quickQuoteFeesDto.getThirdPartyFee())
                .valuationFee(quickQuoteFeesDto.getValuationFee())
                .isAddProductFeesToFacility(quickQuoteFeesDto.getIsAddProductFeesToFacility())
                .intermediaryFeeAmount(quickQuoteFeesDto.getIntermediaryFeeAmount())
                .isAddIntermediaryFeeToLoan(quickQuoteFeesDto.getIsAddIntermediaryFeeToLoan())
                .isAddArrangementFeeSelinaToLoan(quickQuoteFeesDto.getIsAddArrangementFeeSelinaToLoan())
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
