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

package com.selina.lending.api.mapper.common;

import com.selina.lending.api.dto.common.PropertyDetailsDto;
import com.selina.lending.api.dto.dip.request.DIPPropertyDetailsDto;
import com.selina.lending.api.dto.dip.response.PropertyDetailValueDto;
import com.selina.lending.api.dto.dipcc.request.DIPCCPropertyDetailsDto;
import com.selina.lending.api.mapper.config.DIPPropertyDetailsMapperConfig;
import com.selina.lending.httpclient.middleware.dto.common.PropertyDetails;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper (config = DIPPropertyDetailsMapperConfig.class, uses = {PriorChargesMapper.class})
public interface PropertyDetailsMapper {
    PropertyDetailsMapper INSTANCE = Mappers.getMapper(PropertyDetailsMapper.class);

    @InheritConfiguration(name = "mapDipPropertyDetailsDto")
    @Mapping(target = "whenLastPurchased", source = "propertyDetails.whenHasLastPurchased")
    DIPPropertyDetailsDto mapToPropertyDetailsDto (PropertyDetails propertyDetails);

    @InheritConfiguration(name = "mapDipPropertyDetailsDto")
    PropertyDetailValueDto mapToPropertyDetailValueDto (PropertyDetails propertyDetails);

    @InheritConfiguration(name = "mapPropertyDetails")
    @Mapping(target = "whenHasLastPurchased", source = "dipPropertyDetailsDto.whenLastPurchased")
    PropertyDetails mapToPropertyDetails(DIPPropertyDetailsDto dipPropertyDetailsDto);

    @InheritConfiguration(name = "mapPropertyDetails")
    PropertyDetails mapToPropertyDetails(PropertyDetailsDto propertyDetailsDto);

    @InheritConfiguration(name = "mapPropertyDetails")
    @Mapping(target = "whenHasLastPurchased", source = "dipCCPropertyDetailsDto.whenLastPurchased")
    PropertyDetails mapToPropertyDetails(DIPCCPropertyDetailsDto dipCCPropertyDetailsDto);
}
