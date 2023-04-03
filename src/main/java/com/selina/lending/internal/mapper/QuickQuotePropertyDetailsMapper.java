package com.selina.lending.internal.mapper;

import com.selina.lending.internal.dto.quote.QuickQuotePropertyDetailsDto;
import com.selina.lending.internal.service.application.domain.PropertyDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = PriorChargesMapper.class)
public interface QuickQuotePropertyDetailsMapper {
    QuickQuotePropertyDetailsMapper INSTANCE = Mappers.getMapper(QuickQuotePropertyDetailsMapper.class);

    PropertyDetails mapToPropertyDetails(QuickQuotePropertyDetailsDto propertyDetailsDto);
}
