package com.selina.lending.internal.mapper.quote.middleware;

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import com.selina.lending.httpclient.selection.dto.response.Product;
import com.selina.lending.httpclient.middleware.dto.qq.request.QuickQuoteRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {QuickQuoteApplicantMapper.class, LoanInformationMapper.class, TokenService.class, OfferMapper.class})
public abstract class MiddlewareQuickQuoteApplicationRequestMapper {

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
    @Mapping(target = "fees", source = "fees", qualifiedByName = "setDefaultFeesValues")
    @Mapping(target = "offers", source = "products")
    @Mapping(target = "partner", source = "request.partner")
    public abstract QuickQuoteRequest mapToQuickQuoteRequest(QuickQuoteApplicationRequest request,
                                                             List<Product> products, Fees fees);

    @Named("setDefaultFeesValues")
    Fees setDefaultFeesValues(Fees fees) {
        return Fees.builder()
                .isAddAdviceFeeToLoan(fees.getIsAddAdviceFeeToLoan())
                .isAddArrangementFeeToLoan(fees.getIsAddArrangementFeeToLoan())
                .isAddCommissionFeeToLoan(fees.getIsAddCommissionFeeToLoan())
                .isAddThirdPartyFeeToLoan(fees.getIsAddThirdPartyFeeToLoan())
                .isAddValuationFeeToLoan(fees.getIsAddValuationFeeToLoan())
                .adviceFee(fees.getAdviceFee())
                .arrangementFee(fees.getArrangementFee())
                .commissionFee(fees.getCommissionFee())
                .thirdPartyFee(fees.getThirdPartyFee())
                .valuationFee(fees.getValuationFee())
                .isAddProductFeesToFacility(ADD_PRODUCT_FEES_TO_FACILITY)
                .intermediaryFeeAmount(fees.getIntermediaryFeeAmount())
                .isAddIntermediaryFeeToLoan(fees.getIsAddIntermediaryFeeToLoan())
                .addArrangementFeeSelina(fees.getAddArrangementFeeSelina())
                .arrangementFeeDiscountSelina(fees.getArrangementFeeDiscountSelina())
                .build();
    }

}
