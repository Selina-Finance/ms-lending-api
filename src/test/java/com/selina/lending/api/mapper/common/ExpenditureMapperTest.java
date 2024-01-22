package com.selina.lending.api.mapper.common;

import com.selina.lending.api.mapper.MapperBase;
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
}
