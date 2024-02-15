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

import com.selina.lending.api.dto.common.ExpenditureDto;
import com.selina.lending.api.dto.common.IncomeDto;
import com.selina.lending.api.dto.common.IncomeItemDto;
import com.selina.lending.api.dto.common.PriorChargesDto;
import com.selina.lending.api.dto.qq.request.*;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import com.selina.lending.httpclient.selection.dto.request.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class QuickQuoteApplicationRequestMapper {

    private static final String DEFAULT_EXPENDITURE_FREQUENCY = "monthly";

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
                .expenditures(mapExpenditures(quickQuoteApplicationRequest.getExpenditure()))
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

    private static List<Expenditure> mapExpenditures(List<ExpenditureDto> quickQuoteExpenditures) {
        return quickQuoteExpenditures == null ? null : quickQuoteExpenditures.stream()
                .map(QuickQuoteApplicationRequestMapper::mapExpenditure)
                .toList();
    }

    private static Expenditure mapExpenditure(ExpenditureDto quickQuoteExpenditureDto) {
        return quickQuoteExpenditureDto == null ? null : Expenditure.builder()
                .amountDeclared(mapAmountDeclared(quickQuoteExpenditureDto.getAmountDeclared(), quickQuoteExpenditureDto.getFrequency()))
                .expenditureType(quickQuoteExpenditureDto.getExpenditureType())
                .frequency(DEFAULT_EXPENDITURE_FREQUENCY)
                .build();
    }

    private static double roundHalfUp(Double amountDeclared) {
        return Precision.round(amountDeclared, 2);
    }

    private static Double mapAmountDeclared(Double expenditureAmountDeclared, String expenditureFrequency) {
        return Arrays.stream(ExpenditureDto.Frequency.values())
                .filter(frequency -> frequency.getValue().equals(expenditureFrequency))
                .findFirst()
                .map(frequency -> expenditureAmountDeclared * frequency.getMonthlyFactor())
                .orElse(expenditureAmountDeclared);
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
