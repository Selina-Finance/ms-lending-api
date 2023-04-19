package com.selina.lending.messaging.mapper;

import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.mapper.QuickQuoteApplicantMapper;
import com.selina.lending.messaging.event.middleware.MiddlewareCreateApplicationEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {QuickQuoteApplicantMapper.class})
public interface MiddlewareCreateApplicationEventMapper {

    MiddlewareCreateApplicationEventMapper INSTANCE = Mappers.getMapper(MiddlewareCreateApplicationEventMapper.class);


    MiddlewareCreateApplicationEvent mapToMiddlewareCreateApplicationEvent(QuickQuoteApplicationRequest quickQuoteApplicationRequest);
}
