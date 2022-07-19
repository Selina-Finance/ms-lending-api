package com.selina.lending.internal.service.application.dto;

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
public class Incomes {
    private List<Income> incomeList;
    private Boolean doesNotHaveAnyIncome;
    private Boolean expectsFutureIncomeDecrease;
    private String expectsFutureIncomeDecreaseReason;
    private Double contractDayRateVerified;
    private Integer contractDaysWorkedWeeklyVerified;
}
