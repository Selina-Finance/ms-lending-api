package com.selina.lending.api.mapper.qqcf;


import com.selina.lending.api.dto.qqcf.request.QuickQuoteCFApplicationRequest;
import com.selina.lending.api.mapper.common.FeesMapper;
import com.selina.lending.api.mapper.common.LoanInformationMapper;
import com.selina.lending.httpclient.middleware.dto.qqcf.request.QuickQuoteCFRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {QuickQuoteCFApplicantMapper.class, FeesMapper.class, LoanInformationMapper.class, QuickQuoteCFPropertyDetailsMapper.class})
public interface QuickQuoteCFRequestMapper {

    QuickQuoteCFRequestMapper INSTANCE = Mappers.getMapper(QuickQuoteCFRequestMapper.class);
    QuickQuoteCFRequest mapToQuickQuoteCFRequest(QuickQuoteCFApplicationRequest quickQuoteCFApplicationRequest);

}
