package com.selina.lending.internal.mapper.quotecc;


import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.mapper.LoanInformationMapper;
import com.selina.lending.internal.mapper.QuickQuoteApplicantMapper;
import com.selina.lending.internal.mapper.QuickQuotePropertyDetailsMapper;
import com.selina.lending.internal.service.application.domain.quotecc.QuickQuoteCFRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {QuickQuoteApplicantMapper.class, LoanInformationMapper.class, QuickQuotePropertyDetailsMapper.class})
public interface QuickQuoteCFRequestMapper {

    QuickQuoteCFRequestMapper INSTANCE = Mappers.getMapper(QuickQuoteCFRequestMapper.class);
    QuickQuoteCFRequest mapToQuickQuoteCFRequest(QuickQuoteApplicationRequest quickQuoteApplicationRequest);

}
