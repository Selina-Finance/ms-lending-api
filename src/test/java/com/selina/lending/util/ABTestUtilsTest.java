package com.selina.lending.util;

import com.selina.lending.api.mapper.MapperBase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ABTestUtilsTest extends MapperBase {

    @Test
    void whenTestGroupIdIsNotSpecifiedThenSetActualValue() {
        var quickQuoteRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteRequest.setTestGroupId(null);

        ABTestUtils.appendTestGroupId(quickQuoteRequest, "GRO-0001: Group A");

        assertThat(quickQuoteRequest.getTestGroupId(), equalTo("GRO-0001: Group A"));
    }

    @Test
    void whenTestGroupIdExistsThenAppendNewTestGroupId() {
        var quickQuoteRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteRequest.setTestGroupId("GRO-0001: Group A");

        ABTestUtils.appendTestGroupId(quickQuoteRequest, "GRO-0002: Group B");

        assertThat(quickQuoteRequest.getTestGroupId(), equalTo("GRO-0001: Group A, GRO-0002: Group B"));
    }
}
