package com.selina.lending.internal.mapper.quotecf;


import com.selina.lending.api.dto.qqcf.request.QuickQuoteCFApplicationRequest;
import com.selina.lending.internal.mapper.FeesMapper;
import com.selina.lending.internal.mapper.LoanInformationMapper;
import com.selina.lending.httpclient.middleware.dto.qqcf.request.QuickQuoteCFRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {QuickQuoteCFApplicantMapper.class, FeesMapper.class, LoanInformationMapper.class, QuickQuoteCFPropertyDetailsMapper.class})
public interface QuickQuoteCFRequestMapper {

    QuickQuoteCFRequestMapper INSTANCE = Mappers.getMapper(QuickQuoteCFRequestMapper.class);
    QuickQuoteCFRequest mapToQuickQuoteCFRequest(QuickQuoteCFApplicationRequest quickQuoteCFApplicationRequest);

}
