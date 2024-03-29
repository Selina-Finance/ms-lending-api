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

package com.selina.lending.api.mapper.config;

import com.selina.lending.api.dto.dip.response.DIPApplicationDto;
import com.selina.lending.httpclient.middleware.dto.dip.response.Application;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DIPApplicationMapperConfig extends ApplicationMapperConfig {

    @InheritConfiguration(name = "mapApplicationDto")
    void mapDIPApplicationDto(Application application, @MappingTarget DIPApplicationDto dipApplicationDto);

    @InheritConfiguration(name = "mapApplication")
    void mapDIPApplication(DIPApplicationDto dipApplicationDto, @MappingTarget Application application);
}
