package com.selina.lending.internal.mapper.quotecf;


import com.selina.lending.internal.dto.quotecf.QuickQuoteCFApplicationRequest;
import com.selina.lending.internal.mapper.LoanInformationMapper;
import com.selina.lending.internal.mapper.QuickQuotePropertyDetailsMapper;
import com.selina.lending.internal.service.application.domain.quotecf.QuickQuoteCFRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {QuickQuoteCFApplicantMapper.class, LoanInformationMapper.class, QuickQuotePropertyDetailsMapper.class})
public interface QuickQuoteCFRequestMapper {

    QuickQuoteCFRequestMapper INSTANCE = Mappers.getMapper(QuickQuoteCFRequestMapper.class);
    QuickQuoteCFRequest mapToQuickQuoteCFRequest(QuickQuoteCFApplicationRequest quickQuoteCFApplicationRequest);

}
