package com.selina.lending.api.mapper.qqcf;

import com.selina.lending.api.dto.qqcf.request.QuickQuoteCFApplicantDto;
import com.selina.lending.api.mapper.common.AddressMapper;
import com.selina.lending.api.mapper.common.EmploymentMapper;
import com.selina.lending.api.mapper.common.IncomeMapper;
import com.selina.lending.httpclient.middleware.dto.common.Applicant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AddressMapper.class, IncomeMapper.class, EmploymentMapper.class})
public interface QuickQuoteCFApplicantMapper {

    QuickQuoteCFApplicantMapper INSTANCE = Mappers.getMapper(QuickQuoteCFApplicantMapper.class);

    @Mapping(target = "mobilePhoneNumber", source = "mobileNumber")
    Applicant mapToApplicant(QuickQuoteCFApplicantDto applicantDto);
}
