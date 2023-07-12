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

package com.selina.lending.api.mapper.dip;

import com.selina.lending.api.mapper.common.LoanInformationMapper;
import com.selina.lending.api.mapper.common.OfferMapper;
import com.selina.lending.api.mapper.common.PropertyDetailsMapper;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.selina.lending.api.dto.dip.response.DIPApplicationDto;
import com.selina.lending.api.mapper.config.DIPApplicationMapperConfig;
import com.selina.lending.httpclient.middleware.dto.dip.response.Application;

@Mapper(config = DIPApplicationMapperConfig.class , uses = {LoanInformationMapper.class, DIPApplicantMapper.class, OfferMapper.class, PropertyDetailsMapper.class})
public interface ApplicationMapper {
    ApplicationMapper INSTANCE = Mappers.getMapper(ApplicationMapper.class);

    @InheritConfiguration(name = "mapDIPApplicationDto")
    @Mapping(target = "requestType", source = "application.applicationType")
    DIPApplicationDto mapToDIPApplicationDto(Application application);

    @InheritConfiguration(name = "mapDIPApplication")
    @Mapping(target = "applicationType", source = "dipApplicationDto.requestType")
    Application mapToApplication(DIPApplicationDto dipApplicationDto);
}

