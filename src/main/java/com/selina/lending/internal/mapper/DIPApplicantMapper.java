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

package com.selina.lending.internal.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.selina.lending.internal.dto.DIPApplicantDto;
import com.selina.lending.internal.mapper.config.DIPApplicantMapperConfig;
import com.selina.lending.httpclient.middleware.dto.common.Applicant;

@Mapper(config = DIPApplicantMapperConfig.class, uses = {AddressMapper.class,
        ChecklistMapper.class, IncomeMapper.class, EmploymentMapper.class})
public interface DIPApplicantMapper {
    DIPApplicantMapper INSTANCE = Mappers.getMapper(DIPApplicantMapper.class);

    @InheritConfiguration(name = "mapDipApplicant")
    @Mapping(target = "mobilePhoneNumber", source = "dipApplicantDto.mobileNumber")
    Applicant mapToApplicant(DIPApplicantDto dipApplicantDto);

    @InheritConfiguration(name ="mapDipApplicantDto")
    @Mapping(target = "mobileNumber", source = "applicant.mobilePhoneNumber")
    DIPApplicantDto mapToApplicantDto(Applicant applicant);
}
