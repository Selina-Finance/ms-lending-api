package com.selina.lending.api.mapper.qq.middleware;

import com.selina.lending.api.mapper.MapperBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
public class ExpenditureMapperTest extends MapperBase {

    @Autowired
    private ExpenditureMapper expenditureMapper;

    @Test
    void shouldMapExpenditure() {
        var expenditure = expenditureMapper.mapToExpenditure(getExpenditureDto());

        assertThat(expenditure.getExpenditureType(), equalTo(EXPENDITURE_TYPE));
        assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED));
        assertThat(expenditure.getFrequency(), equalTo(EXPENDITURE_FREQUENCY));
    }

    @Test
    void whenExpenditureFrequencyIsNotSpecifiedThenMapItToMonthly() {
        var expenditureDto = getExpenditureDto();
        expenditureDto.setFrequency(null);

        var expenditure = expenditureMapper.mapToExpenditure(expenditureDto);

        assertThat(expenditure.getExpenditureType(), equalTo(EXPENDITURE_TYPE));
        assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED));
        assertThat(expenditure.getFrequency(), equalTo("monthly"));
    }
}
