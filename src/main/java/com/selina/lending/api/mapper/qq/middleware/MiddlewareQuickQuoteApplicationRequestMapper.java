package com.selina.lending.api.mapper.qq.middleware;

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import com.selina.lending.httpclient.middleware.dto.qq.request.QuickQuoteRequest;
import com.selina.lending.httpclient.selection.dto.response.Product;
import com.selina.lending.service.TokenService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {QuickQuoteApplicantMapper.class, LoanInformationMapper.class, TokenService.class, OfferMapper.class,
                ExpenditureMapper.class})
public abstract class MiddlewareQuickQuoteApplicationRequestMapper {

    @Autowired
    protected TokenService tokenService;

    private static final String PRODUCT_CODE = "QQ01";
    private static final String SOURCE = "LendingAPI";
    private static final String APPLICATION_TYPE = "QuickQuote";
    private static final String HAS_GIVEN_CONSENT_FOR_MARKETING_COMMUNICATIONS = "false";

    @Mapping(target = "sourceAccount", expression = "java(tokenService.retrieveSourceAccount())")
    @Mapping(target = "source", constant = SOURCE)
    @Mapping(target = "applicationType", constant = APPLICATION_TYPE)
    @Mapping(target = "productCode", constant = PRODUCT_CODE)
    @Mapping(target = "applicants", source = "request.applicants")
    @Mapping(target = "hasGivenConsentForMarketingCommunications", constant = HAS_GIVEN_CONSENT_FOR_MARKETING_COMMUNICATIONS)
    @Mapping(target = "fees", source = "fees")
    @Mapping(target = "offers", source = "products")
    @Mapping(target = "partner", source = "request.partner")
    @Mapping(target = "expenditure", source = "request.expenditure")
    public abstract QuickQuoteRequest mapToQuickQuoteRequest(QuickQuoteApplicationRequest request,
                                                             List<Product> products, Fees fees);
}
