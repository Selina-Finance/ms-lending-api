package com.selina.lending.internal.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Income {
    private List<IncomeItem> income;
    private Boolean doesNotHaveAnyIncome;
    private Boolean expectsFutureIncomeDecrease;
    private String expectsFutureIncomeDecreaseReason;
    private Double contractDayRateVerified;
    private Integer contractDaysWorkedWeeklyVerified;
}
