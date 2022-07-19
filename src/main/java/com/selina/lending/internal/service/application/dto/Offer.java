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
public class Offer {
    private String id;
    private Boolean active;
    private Boolean productFeeCanAdd;
    private Double aprc;
    private Double cltv;
    private Double offerBalance;
    private Double initialTerm;
    private Double initialRate;
    private Double brokerFeesIncluded;
    private Double reversionTerm;
    private Double svr;
    private Double procFee;
    private Double lti;
    private Double ltvCap;
    private Double totalAmountRepaid;
    private Double monthlyAffordabilityStatus;
    private Double initialPayment;
    private Double reversionPayment;
    private Double affordabilityDeficit;
    private String productCode;
    private String product;
    private String decision;
    private Double productFee;
    private boolean productFeeAddedToLoan;
    private boolean selected;
    private int brokerFeesUpfront;
    private int externalSettlement;
    private String eSignUrl;
    private boolean hasFee;
    private Checklist checklist;
    private String plan;
    private String tier;
    private int maximumAdvance;
    private int maximumAdvancePrime;
    private boolean isVariable;
    private List<RuleOutcome> ruleOutcomes;
}
