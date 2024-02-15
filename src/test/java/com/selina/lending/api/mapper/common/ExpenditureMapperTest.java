package com.selina.lending.api.mapper.common;

import com.selina.lending.api.dto.common.ExpenditureDto;
import com.selina.lending.api.mapper.MapperBase;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ExpenditureMapperTest extends MapperBase {

    @Test
    void shouldMapExpenditure() {
        var expenditure = ExpenditureMapper.INSTANCE.mapToExpenditure(getExpenditureDto());

        assertThat(expenditure.getFrequency(), equalTo(EXPENDITURE_FREQUENCY));
        assertThat(expenditure.getBalanceDeclared(), equalTo(EXPENDITURE_BALANCE_DECLARED));
        assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED));
        assertThat(expenditure.getPaymentVerified(), equalTo(EXPENDITURE_PAYMENT_VERIFIED));
        assertThat(expenditure.getAmountVerified(), equalTo(EXPENDITURE_AMOUNT_VERIFIED));
        assertThat(expenditure.getExpenditureType(), equalTo(EXPENDITURE_TYPE));
    }

    @Test
    void whenExpenditureFrequencyIsNotSpecifiedThenDefaultToMonthly() {
        ExpenditureDto expenditureDto = getExpenditureDto();
        expenditureDto.setFrequency(null);

        var expenditure = ExpenditureMapper.INSTANCE.mapToExpenditure(expenditureDto);
        assertThat(expenditure.getFrequency(), equalTo("monthly"));
    }

    @Test
    void whenExpenditureFrequencyIsInvalidThenDefaultToMonthly() {
        ExpenditureDto expenditureDto = getExpenditureDto();
        expenditureDto.setFrequency("invalid-frequency");

        var expenditure = ExpenditureMapper.INSTANCE.mapToExpenditure(expenditureDto);
        assertThat(expenditure.getFrequency(), equalTo("monthly"));
        assertThat(expenditure.getAmountDeclared(), equalTo(roundHalfUp(EXPENDITURE_AMOUNT_DECLARED)));
    }

    @Test
    void shouldMapDailyExpenditureToMonthly() {
        ExpenditureDto expenditureDto = getExpenditureDto();
        expenditureDto.setFrequency("daily");
        var dailyFrequencyFactor = 30;

        var expenditure = ExpenditureMapper.INSTANCE.mapToExpenditure(expenditureDto);
        assertThat(expenditure.getFrequency(), equalTo("monthly"));
        assertThat(expenditure.getAmountDeclared(), equalTo(roundHalfUp(EXPENDITURE_AMOUNT_DECLARED * dailyFrequencyFactor)));
    }

    @Test
    void shouldMapWeeklyExpenditureToMonthly() {
        ExpenditureDto expenditureDto = getExpenditureDto();
        expenditureDto.setFrequency("weekly");
        var weeklyFrequencyFactor = 4.33;

        var expenditure = ExpenditureMapper.INSTANCE.mapToExpenditure(expenditureDto);
        assertThat(expenditure.getFrequency(), equalTo("monthly"));
        assertThat(expenditure.getAmountDeclared(), equalTo(roundHalfUp(EXPENDITURE_AMOUNT_DECLARED * weeklyFrequencyFactor)));
    }

    @Test
    void shouldMapBiWeeklyExpenditureToMonthly() {
        ExpenditureDto expenditureDto = getExpenditureDto();
        expenditureDto.setFrequency("bi-weekly");
        var biWeeklyFrequencyFactor = 2.166;

        var expenditure = ExpenditureMapper.INSTANCE.mapToExpenditure(expenditureDto);
        assertThat(expenditure.getFrequency(), equalTo("monthly"));
        assertThat(expenditure.getAmountDeclared(), equalTo(roundHalfUp(EXPENDITURE_AMOUNT_DECLARED * biWeeklyFrequencyFactor)));
    }

    @Test
    void shouldMapMonthlyExpenditureToMonthly() {
        ExpenditureDto expenditureDto = getExpenditureDto();
        expenditureDto.setFrequency("monthly");
        var monthlyFrequencyFactor = 1;

        var expenditure = ExpenditureMapper.INSTANCE.mapToExpenditure(expenditureDto);
        assertThat(expenditure.getFrequency(), equalTo("monthly"));
        assertThat(expenditure.getAmountDeclared(), equalTo(roundHalfUp(EXPENDITURE_AMOUNT_DECLARED * monthlyFrequencyFactor)));
    }

    @Test
    void shouldMapQuarterlyExpenditureToMonthly() {
        ExpenditureDto expenditureDto = getExpenditureDto();
        expenditureDto.setFrequency("quarterly");
        var quarterlyFrequencyFactor = 0.25;

        var expenditure = ExpenditureMapper.INSTANCE.mapToExpenditure(expenditureDto);
        assertThat(expenditure.getFrequency(), equalTo("monthly"));
        assertThat(expenditure.getAmountDeclared(), equalTo(roundHalfUp(EXPENDITURE_AMOUNT_DECLARED * quarterlyFrequencyFactor)));
    }

    @Test
    void shouldMapSemiAnnuallyExpenditureToMonthly() {
        ExpenditureDto expenditureDto = getExpenditureDto();
        expenditureDto.setFrequency("semi-annually");
        var semiAnnuallyFrequencyFactor = 0.125;

        var expenditure = ExpenditureMapper.INSTANCE.mapToExpenditure(expenditureDto);
        assertThat(expenditure.getFrequency(), equalTo("monthly"));
        assertThat(expenditure.getAmountDeclared(), equalTo(roundHalfUp(EXPENDITURE_AMOUNT_DECLARED * semiAnnuallyFrequencyFactor)));
    }

    @Test
    void shouldMapAnnuallyExpenditureToMonthly() {
        ExpenditureDto expenditureDto = getExpenditureDto();
        expenditureDto.setFrequency("annually");
        var annuallyFrequencyFactor = 0.0833333;

        var expenditure = ExpenditureMapper.INSTANCE.mapToExpenditure(expenditureDto);
        assertThat(expenditure.getFrequency(), equalTo("monthly"));
        assertThat(expenditure.getAmountDeclared(), equalTo(roundHalfUp(EXPENDITURE_AMOUNT_DECLARED * annuallyFrequencyFactor)));
    }

    private static double roundHalfUp(Double amountDeclared) {
        return Precision.round(amountDeclared, 2);
    }
}
