/*
 * Copyright 2022 Selina Finance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.selina.lending.api.mapper.applicant;

import com.selina.lending.api.dto.application.response.ApplicantResponseDto;
import com.selina.lending.api.mapper.common.AddressMapper;
import com.selina.lending.api.mapper.common.ChecklistMapper;
import com.selina.lending.api.mapper.common.DocumentMapper;
import com.selina.lending.api.mapper.common.EmploymentMapper;
import com.selina.lending.api.mapper.common.IncomeMapper;
import com.selina.lending.httpclient.middleware.dto.common.Applicant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AddressMapper.class, ChecklistMapper.class, IncomeMapper.class,
        EmploymentMapper.class, CreditCheckMapper.class, DocumentMapper.class})
public interface ApplicantResponseMapper {
    ApplicantResponseMapper INSTANCE = Mappers.getMapper(ApplicantResponseMapper.class);

    @Mapping(target = "mobileNumber", source = "applicant.mobilePhoneNumber")
    ApplicantResponseDto mapToApplicantResponseDto(Applicant applicant);
}
