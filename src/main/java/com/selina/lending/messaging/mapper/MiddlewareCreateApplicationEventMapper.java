package com.selina.lending.messaging.mapper;

import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.mapper.QuickQuoteApplicantMapper;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.messaging.event.middleware.MiddlewareCreateApplicationEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {QuickQuoteApplicantMapper.class, TokenService.class})
public abstract class MiddlewareCreateApplicationEventMapper {

    @Autowired
    protected TokenService tokenService;

    private static final String PRODUCT_CODE = "QQ01";
    private static final String SOURCE = "LendingAPI";

    @Mapping(target = "productCode", constant = PRODUCT_CODE)
    @Mapping(target = "sourceAccount", expression = "java(tokenService.retrieveSourceAccount())")
    @Mapping(target = "source", constant = SOURCE)
    public abstract MiddlewareCreateApplicationEvent mapToMiddlewareCreateApplicationEvent(QuickQuoteApplicationRequest quickQuoteApplicationRequest);
}
