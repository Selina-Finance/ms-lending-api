package com.selina.lending.internal.mapper.quote.middleware;

import com.selina.lending.api.dto.common.AddressDto;
import com.selina.lending.httpclient.middleware.dto.common.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    @Mapping(target = "fromDate", source = "address.from")
    @Mapping(target = "toDate", source = "address.to")
    AddressDto mapToAddressDto(Address address);

    @Mapping(target = "from", source = "addressDto.fromDate")
    @Mapping(target = "to", source = "addressDto.toDate")
    Address mapToAddress(AddressDto addressDto);
}
