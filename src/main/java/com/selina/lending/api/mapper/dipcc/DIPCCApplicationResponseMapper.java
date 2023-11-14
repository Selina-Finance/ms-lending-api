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

import com.selina.lending.api.dto.dipcc.response.DIPCCApplicationResponse;
import com.selina.lending.api.mapper.applicant.SalesforceMapper;
import com.selina.lending.api.mapper.common.CreditCommitmentMapper;
import com.selina.lending.api.mapper.dip.ApplicationMapper;
import com.selina.lending.httpclient.middleware.dto.dip.response.ApplicationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ApplicationMapper.class, SalesforceMapper.class, CreditCommitmentMapper.class})
public interface DIPCCApplicationResponseMapper {
    DIPCCApplicationResponseMapper INSTANCE = Mappers.getMapper(DIPCCApplicationResponseMapper.class);

    @Mapping(target= "requestType", source = "applicationResponse.applicationType")
    DIPCCApplicationResponse mapToApplicationResponseDto(ApplicationResponse applicationResponse);
}
