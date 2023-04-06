package com.selina.lending.internal.mapper.quotecc;

import com.selina.lending.internal.dto.quote.QuickQuoteResponse;
import com.selina.lending.internal.service.application.domain.quotecc.QuickQuoteCCResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {OfferMapper.class})
public interface QuickQuoteCCResponseMapper {

    QuickQuoteCCResponseMapper INSTANCE = Mappers.getMapper(QuickQuoteCCResponseMapper.class);
    QuickQuoteResponse mapToQuickQuoteResponse(QuickQuoteCCResponse quickQuoteResponse);

}
