package com.selina.lending.messaging.mapper.middleware;

import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.internal.service.application.domain.Fees;
import com.selina.lending.internal.service.application.domain.Offer;
import com.selina.lending.internal.service.application.domain.quote.Product;
import com.selina.lending.messaging.event.middleware.MiddlewareCreateApplicationEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {QuickQuoteApplicantMapper.class, LoanInformationMapper.class, TokenService.class})
public abstract class MiddlewareCreateApplicationEventMapper {

    @Autowired
    protected TokenService tokenService;

    private static final String PRODUCT_CODE = "QQ01";
    private static final String SOURCE = "LendingAPI";
    private static final String APPLICATION_TYPE = "QuickQuote";
    private static final String HAS_GIVEN_CONSENT_FOR_MARKETING_COMMUNICATIONS = "false";
    private static final boolean ADD_PRODUCT_FEES_TO_FACILITY = false;

    @Mapping(target = "sourceAccount", expression = "java(tokenService.retrieveSourceAccount())")
    @Mapping(target = "source", constant = SOURCE)
    @Mapping(target = "applicationType", constant = APPLICATION_TYPE)
    @Mapping(target = "productCode", constant = PRODUCT_CODE)
    @Mapping(target = "applicants", source = "request.applicants")
    @Mapping(target = "hasGivenConsentForMarketingCommunications", constant = HAS_GIVEN_CONSENT_FOR_MARKETING_COMMUNICATIONS)
    @Mapping(target = "fees", expression = "java(this.createDefaultFees())")
    @Mapping(target = "offers", source = "products")
    public abstract MiddlewareCreateApplicationEvent mapToMiddlewareCreateApplicationEvent(QuickQuoteApplicationRequest request,
                                                                                           List<Product> products);

    Fees createDefaultFees() {
        return Fees.builder()
                .isAddProductFeesToFacility(ADD_PRODUCT_FEES_TO_FACILITY)
                .build();
    }

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
    @Mapping(target = "ercProfile", source = "product.ercProfile")
    @Mapping(target = "ercShortCode", source = "product.ercShortCode")
    @Mapping(target = "ercPeriodYears", source = "product.offer.ercPeriodYears")
    @Mapping(target = "term", source = "product.offer.term")
    @Mapping(target = "ear", source = "product.offer.ear")
    @Mapping(target = "maximumLoanAmount", source = "product.offer.maximumLoanAmount")
    @Mapping(target = "requestedLoanAmount", source = "product.offer.requestedLoanAmount")
    @Mapping(target = "hasErc", source = "product.hasErc")
    @Mapping(target = "ercData", source = "product.offer.ercData")
    public abstract Offer mapToOffer(Product product);
}
