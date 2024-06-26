package com.selina.lending.api.mapper.qq.middleware;

import com.selina.lending.httpclient.middleware.dto.common.Offer;
import com.selina.lending.httpclient.adp.dto.response.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OfferMapper {

    @Mapping(target = "id", source = "product.offer.id")
    @Mapping(target = "productFeeCanAdd", source = "product.offer.canAddProductFee")
    @Mapping(target = "aprc", source = "product.offer.aprc")
    @Mapping(target = "cltv", source = "product.offer.cltv")
    @Mapping(target = "offerBalance", source = "product.offer.offerBalance")
    @Mapping(target = "initialTerm", source = "product.offer.initialTerm")
    @Mapping(target = "initialRate", source = "product.offer.initialRate")
    @Mapping(target = "brokerFeesIncluded", source = "product.offer.brokerFeesIncluded")
    @Mapping(target = "reversionTerm", source = "product.offer.reversionTerm")
    @Mapping(target = "svr", source = "product.offer.svr")
    @Mapping(target = "procFee", source = "product.offer.procFee")
    @Mapping(target = "lti", source = "product.offer.lti")
    @Mapping(target = "ltvCap", source = "product.offer.ltvCap")
    @Mapping(target = "totalAmountRepaid", source = "product.offer.totalAmountRepaid")
    @Mapping(target = "initialPayment", source = "product.offer.initialPayment")
    @Mapping(target = "reversionPayment", source = "product.offer.reversionPayment")
    @Mapping(target = "affordabilityDeficit", source = "product.offer.affordabilityDeficit")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "product", source = "product.name")
    @Mapping(target = "decision", source = "product.offer.decision")
    @Mapping(target = "productFee", source = "product.offer.productFee")
    @Mapping(target = "productFeeAddedToLoan", source = "product.offer.hasProductFeeAddedToLoan")
    @Mapping(target = "brokerFeesUpfront", source = "product.offer.brokerFeesUpfront")
    @Mapping(target = "hasFee", source = "product.offer.hasFee")
    @Mapping(target = "isVariable", source = "product.isVariable")
    @Mapping(target = "category", source = "product.category")
    @Mapping(target = "family", source = "product.family")
    @Mapping(target = "maximumBalanceEsis", source = "product.offer.maximumBalanceEsis")
    @Mapping(target = "maxErc", source = "product.offer.maxErc")
    @Mapping(target = "ercPeriodYears", source = "product.offer.ercPeriodYears")
    @Mapping(target = "term", source = "product.offer.term")
    @Mapping(target = "ear", source = "product.offer.ear")
    @Mapping(target = "maximumLoanAmount", source = "product.offer.maximumLoanAmount")
    @Mapping(target = "requestedLoanAmount", source = "product.offer.requestedLoanAmount")
    @Mapping(target = "hasErc", source = "product.hasErc")
    @Mapping(target = "ercData", source = "product.offer.ercData")
    @Mapping(target = "expiryDate", source = "product.offer.expiryDate")
    @Mapping(target = "dtir", source = "product.offer.dtir")
    @Mapping(target = "bbr", source = "product.offer.bbr")
    @Mapping(target = "initialMargin", source = "product.offer.initialMargin")
    @Mapping(target = "poundPaidPerBorrowed", source = "product.offer.poundPaidPerBorrowed")
    @Mapping(target = "monthlyPaymentStressed", source = "product.offer.monthlyPaymentStressed")
    @Mapping(target = "baseRateStressed", source = "product.offer.baseRateStressed")
    @Mapping(target = "initialRateMinimum", source = "product.offer.initialRateMinimum")
    @Mapping(target = "reversionMargin", source = "product.offer.reversionMargin")
    @Mapping(target = "reversionRateMinimum", source = "product.offer.reversionRateMinimum")
    @Mapping(target = "drawdownTerm", source = "product.offer.drawdownTerm")
    @Mapping(target = "esisLtvCappedBalance", source = "product.offer.esisLtvCappedBalance")
    @Mapping(target = "incomePrimaryApplicant", source = "product.offer.incomePrimaryApplicant")
    @Mapping(target = "incomeJointApplicant", source = "product.offer.incomeJointApplicant")
    @Mapping(target = "daysUntilInitialDrawdown", source = "product.offer.daysUntilInitialDrawdown")
    @Mapping(target = "fixedTermYears", source = "product.offer.fixedTermYears")
    @Mapping(target = "minimumInitialDrawdown", source = "product.offer.minimumInitialDrawdown")
    @Mapping(target = "offerValidity", source = "product.offer.offerValidity")
    @Mapping(target = "esisLoanAmount", source = "product.offer.esisLoanAmount")
    @Mapping(target = "propertyValuation", source = "product.offer.propertyValuation")
    @Mapping(target = "arrangementFeeSelina", source = "product.offer.arrangementFeeSelina")
    @Mapping(target = "netLoanAmount", source = "product.offer.netLoanAmount")
    Offer mapToOffer(Product product);
}
