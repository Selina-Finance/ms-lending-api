package com.selina.lending.internal.mapper.quote.middleware;


import com.selina.lending.internal.dto.quote.QuickQuoteApplicantDto;
import com.selina.lending.httpclient.middleware.dto.common.Applicant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {AddressMapper.class, IncomeMapper.class})
public interface QuickQuoteApplicantMapper {

    @Mapping(target = "mobilePhoneNumber", source = "applicantDto.mobileNumber")
    Applicant mapToApplicant(QuickQuoteApplicantDto applicantDto);
}
