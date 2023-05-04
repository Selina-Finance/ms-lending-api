package com.selina.lending.internal.mapper.quotecf;

import com.selina.lending.internal.dto.quotecf.QuickQuoteCFApplicantDto;
import com.selina.lending.internal.mapper.AddressMapper;
import com.selina.lending.internal.mapper.EmploymentMapper;
import com.selina.lending.internal.mapper.IncomeMapper;
import com.selina.lending.internal.service.application.domain.Applicant;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AddressMapper.class, IncomeMapper.class, EmploymentMapper.class})
public interface QuickQuoteCFApplicantMapper {

    QuickQuoteCFApplicantMapper INSTANCE = Mappers.getMapper(QuickQuoteCFApplicantMapper.class);

    @InheritConfiguration(name = "mapApplicant")
    @Mapping(target = "mobilePhoneNumber", source = "applicantDto.mobileNumber")
    Applicant mapToApplicant(QuickQuoteCFApplicantDto applicantDto);
}
