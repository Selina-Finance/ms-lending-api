package com.selina.lending.internal.mapper.quotecf;

import com.selina.lending.api.dto.qq.response.QuickQuoteResponse;
import com.selina.lending.httpclient.middleware.dto.qqcf.response.QuickQuoteCFResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {OfferMapper.class})
public interface QuickQuoteCFResponseMapper {

    QuickQuoteCFResponseMapper INSTANCE = Mappers.getMapper(QuickQuoteCFResponseMapper.class);
    QuickQuoteResponse mapToQuickQuoteResponse(QuickQuoteCFResponse quickQuoteResponse);

}
