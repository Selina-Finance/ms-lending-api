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

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.mapper.config.DIPApplicationRequestMapperConfig;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;

@Mapper(config = DIPApplicationRequestMapperConfig.class, uses = {DIPApplicantMapper.class, FeesMapper.class, LoanInformationMapper.class, PropertyDetailsMapper.class})
public interface DIPApplicationRequestMapper {
    DIPApplicationRequestMapper INSTANCE = Mappers.getMapper(DIPApplicationRequestMapper.class);

    @InheritConfiguration(name = "mapDipApplicationRequest")
    @Mapping(target= "applicationType", source = "dipApplicationRequest.requestType")
    ApplicationRequest mapToApplicationRequest(DIPApplicationRequest dipApplicationRequest);

    @InheritConfiguration(name = "mapApplicationRequest")
    @Mapping(target= "applicationType", source = "applicationRequest.requestType")
    ApplicationRequest mapToApplicationRequest(com.selina.lending.internal.dto.ApplicationRequest applicationRequest);
}
