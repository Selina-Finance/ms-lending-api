package com.selina.lending.api.mapper.qqcf;

import com.selina.lending.api.dto.qqcf.request.QuickQuoteCFPropertyDetailsDto;
import com.selina.lending.api.mapper.common.PriorChargesMapper;
import com.selina.lending.httpclient.middleware.dto.common.PropertyDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = PriorChargesMapper.class)
public interface QuickQuoteCFPropertyDetailsMapper {
    QuickQuoteCFPropertyDetailsMapper INSTANCE = Mappers.getMapper(QuickQuoteCFPropertyDetailsMapper.class);

    PropertyDetails mapToPropertyDetails(QuickQuoteCFPropertyDetailsDto propertyDetailsDto);
}
