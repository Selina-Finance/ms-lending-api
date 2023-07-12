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

package com.selina.lending.api.mapper.dipcc;

import com.selina.lending.api.dto.dipcc.request.DIPCCApplicationRequest;
import com.selina.lending.api.mapper.common.ExpenditureMapper;
import com.selina.lending.api.mapper.common.FeesMapper;
import com.selina.lending.api.mapper.common.LoanInformationMapper;
import com.selina.lending.api.mapper.common.PropertyDetailsMapper;
import com.selina.lending.api.mapper.config.DIPCCApplicationRequestMapperConfig;
import com.selina.lending.api.mapper.dip.DIPApplicantMapper;
import com.selina.lending.httpclient.middleware.dto.dip.request.ApplicationRequest;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = DIPCCApplicationRequestMapperConfig.class, uses = {DIPApplicantMapper.class, FeesMapper.class, LoanInformationMapper.class, PropertyDetailsMapper.class, ExpenditureMapper.class})
public interface DIPCCApplicationRequestMapper {
    DIPCCApplicationRequestMapper INSTANCE = Mappers.getMapper(DIPCCApplicationRequestMapper.class);

    @InheritConfiguration(name = "mapDipCCApplicationRequest")
    ApplicationRequest mapToApplicationRequest(DIPCCApplicationRequest dipApplicationRequest);
}