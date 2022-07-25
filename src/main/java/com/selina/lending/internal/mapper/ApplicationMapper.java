/*
 *   Copyright 2022 Selina Finance
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package com.selina.lending.internal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.selina.lending.internal.dto.ApplicationDto;
import com.selina.lending.internal.service.application.domain.Application;

@Mapper(uses = {LoanInformationMapper.class, ApplicantMapper.class, OfferMapper.class, PropertyDetailsMapper.class})
public interface ApplicationMapper {
    ApplicationMapper INSTANCE = Mappers.getMapper(ApplicationMapper.class);

    @Mapping(target = "requestType", source = "application.applicationType")
    ApplicationDto mapToApplicationDto(Application application);

    @Mapping(target = "applicationType", source = "applicationDto.requestType")
    Application mapToApplication(ApplicationDto applicationDto);
}

