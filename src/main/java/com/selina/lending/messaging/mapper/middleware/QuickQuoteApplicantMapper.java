package com.selina.lending.messaging.mapper.middleware;

import com.selina.lending.internal.dto.quote.QuickQuoteApplicantDto;
import com.selina.lending.internal.service.application.domain.Applicant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {AddressMapper.class})
public interface QuickQuoteApplicantMapper {

    @Mapping(target = "mobilePhoneNumber", source = "applicantDto.mobileNumber")
    Applicant mapToApplicant(QuickQuoteApplicantDto applicantDto);
}
