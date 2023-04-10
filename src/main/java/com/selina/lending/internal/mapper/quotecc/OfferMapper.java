package com.selina.lending.internal.mapper.quotecc;

import com.selina.lending.internal.dto.quote.ProductOfferDto;
import com.selina.lending.internal.mapper.ErcMapper;
import com.selina.lending.internal.service.application.domain.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ErcMapper.class})
public interface OfferMapper {

    @Mapping(source = "offer.productCode", target = "code")
    @Mapping(source = "offer.product", target = "name")
    @Mapping(source = "offer.productFeeAddedToLoan", target = "hasProductFeeAddedToLoan")
    ProductOfferDto mapToProductOfferDto(Offer offer);
}
