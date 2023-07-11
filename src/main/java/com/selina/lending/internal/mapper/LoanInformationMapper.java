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
import org.mapstruct.factory.Mappers;

import com.selina.lending.internal.dto.AdvancedLoanInformationDto;
import com.selina.lending.internal.dto.LoanInformationDto;
import com.selina.lending.internal.mapper.config.AdvancedLoanInformationMapperConfig;
import com.selina.lending.httpclient.middleware.dto.common.LoanInformation;

@Mapper(config = AdvancedLoanInformationMapperConfig.class, uses = {FacilityMapper.class})
public interface LoanInformationMapper {
    LoanInformationMapper INSTANCE = Mappers.getMapper(LoanInformationMapper.class);

    @InheritConfiguration(name = "mapAdvancedLoanInformationDto")
    AdvancedLoanInformationDto mapToLoanInformationDto(LoanInformation loanInformation);

    @InheritConfiguration(name = "mapAdvancedLoanInformation")
    LoanInformation mapToLoanInformation(AdvancedLoanInformationDto advancedLoanInformationDto);

    @InheritConfiguration(name = "mapLoanInformation")
    LoanInformation mapToLoanInformation(LoanInformationDto loanInformationDto);
}
