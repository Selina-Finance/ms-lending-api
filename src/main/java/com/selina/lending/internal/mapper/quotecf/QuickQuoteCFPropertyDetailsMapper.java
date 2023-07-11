package com.selina.lending.internal.mapper.quotecf;

import com.selina.lending.internal.dto.quotecf.QuickQuoteCFPropertyDetailsDto;
import com.selina.lending.internal.mapper.PriorChargesMapper;
import com.selina.lending.httpclient.middleware.dto.common.PropertyDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = PriorChargesMapper.class)
public interface QuickQuoteCFPropertyDetailsMapper {
    QuickQuoteCFPropertyDetailsMapper INSTANCE = Mappers.getMapper(QuickQuoteCFPropertyDetailsMapper.class);

    PropertyDetails mapToPropertyDetails(QuickQuoteCFPropertyDetailsDto propertyDetailsDto);
}
