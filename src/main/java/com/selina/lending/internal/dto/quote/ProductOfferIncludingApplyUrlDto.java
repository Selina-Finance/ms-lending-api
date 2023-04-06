package com.selina.lending.internal.dto.quote;

import com.selina.lending.internal.dto.ErcDto;
import lombok.Data;

import java.util.List;

@Data
public class ProductOfferIncludingApplyUrlDto{
    String id;
    String code;
    String name;
    Double requestedLoanAmount;
    Double maximumLoanAmount;
    Double offerBalance;
    Double totalAmountRepaid;
    Boolean hasFee;
    Double productFee;
    Boolean hasProductFeeAddedToLoan;
    Boolean isVariable;
    Integer term;
    Integer reversionTerm;
    Integer initialTerm;
    Double initialRate;
    Double initialPayment;
    Double aprc;
    Double ear;
    Double svr;
    String family;
    String category;
    Double ltvCap;
    String decision;
    Boolean hasErc;
    Double maxErc;
    String ercShortCode;
    Integer ercPeriodYears;
    List<ErcDto> ercData;
    String applyUrl;
}
