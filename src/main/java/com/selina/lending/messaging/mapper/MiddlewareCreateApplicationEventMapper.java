package com.selina.lending.messaging.mapper;

import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.mapper.QuickQuoteApplicantMapper;
import com.selina.lending.messaging.event.middleware.MiddlewareCreateApplicationEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {QuickQuoteApplicantMapper.class})
public interface MiddlewareCreateApplicationEventMapper {

    String PRODUCT_CODE = "QQ01";

    @Mapping(target = "productCode", constant = PRODUCT_CODE)
    MiddlewareCreateApplicationEvent mapToMiddlewareCreateApplicationEvent(QuickQuoteApplicationRequest quickQuoteApplicationRequest);
}
