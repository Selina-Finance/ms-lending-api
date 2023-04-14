package com.selina.lending.internal.mapper.quotecc;

import com.selina.lending.internal.dto.quote.QuickQuoteResponse;
import com.selina.lending.internal.service.application.domain.quotecc.QuickQuoteCFResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {OfferMapper.class})
public interface QuickQuoteCFResponseMapper {

    QuickQuoteCFResponseMapper INSTANCE = Mappers.getMapper(QuickQuoteCFResponseMapper.class);
    QuickQuoteResponse mapToQuickQuoteResponse(QuickQuoteCFResponse quickQuoteResponse);

}
