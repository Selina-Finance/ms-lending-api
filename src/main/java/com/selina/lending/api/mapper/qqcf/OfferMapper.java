package com.selina.lending.api.mapper.qqcf;

import com.selina.lending.api.dto.qq.response.ProductOfferDto;
import com.selina.lending.api.mapper.common.ErcMapper;
import com.selina.lending.httpclient.middleware.dto.common.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ErcMapper.class})
public interface OfferMapper {

    @Mapping(source = "offer.productCode", target = "code")
    @Mapping(source = "offer.product", target = "name")
    @Mapping(source = "offer.productFeeAddedToLoan", target = "hasProductFeeAddedToLoan")
    @Mapping(source = "ercShortcode", target = "ercShortCode")
    ProductOfferDto mapToProductOfferDto(Offer offer);
}
