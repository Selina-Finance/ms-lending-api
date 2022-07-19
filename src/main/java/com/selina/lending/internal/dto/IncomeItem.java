package com.selina.lending.internal.dto;

import java.util.Date;

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
public class IncomeItem {
    private Double amount;
    private String type;
    private String status;
    private String noIncomeSource;
    private String docRequirements;
    private Double amountVerified;
    private Date incomeDate;
    private String relatedYear;
    private String frequency;
    private Double contractDaysWorkedWeeklyReported;
    private Double contractDayRateReported;
}
