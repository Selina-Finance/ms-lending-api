package com.selina.lending.internal.mapper;

import com.selina.lending.internal.dto.quote.QuickQuoteApplicantDto;
import com.selina.lending.internal.service.application.domain.Applicant;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AddressMapper.class, IncomeMapper.class, EmploymentMapper.class})
public interface QuickQuoteApplicantMapper{

    QuickQuoteApplicantMapper INSTANCE = Mappers.getMapper(QuickQuoteApplicantMapper.class);

    @InheritConfiguration(name = "mapApplicant")
    @Mapping(target = "mobilePhoneNumber", source = "applicantDto.mobileNumber")
    Applicant mapToApplicant(QuickQuoteApplicantDto applicantDto);
}
