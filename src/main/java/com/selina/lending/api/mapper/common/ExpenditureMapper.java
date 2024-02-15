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

import com.selina.lending.api.dto.common.ExpenditureDto;
import com.selina.lending.httpclient.middleware.dto.common.Expenditure;
import org.apache.commons.math3.util.Precision;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;

@Mapper
public interface ExpenditureMapper {

    String DEFAULT_EXPENDITURE_FREQUENCY = "monthly";

    ExpenditureMapper INSTANCE = Mappers.getMapper(ExpenditureMapper.class);

    ExpenditureDto mapToExpenditureDto(Expenditure expenditure);

    @Mapping(target = "frequency", constant = DEFAULT_EXPENDITURE_FREQUENCY)
    @Mapping(target = "amountDeclared", source = "expenditureDto", qualifiedByName = "mapAmountDeclared")
    Expenditure mapToExpenditure(ExpenditureDto expenditureDto);

    @Named("mapAmountDeclared")
    static Double mapAmountDeclared(ExpenditureDto expenditureDto) {
        return Arrays.stream(ExpenditureDto.Frequency.values())
                .filter(frequency -> frequency.getValue().equals(expenditureDto.getFrequency()))
                .findFirst()
                .map(frequency -> expenditureDto.getAmountDeclared() * frequency.getMonthlyFactor())
                .map(ExpenditureMapper::roundHalfUp)
                .orElse(roundHalfUp(expenditureDto.getAmountDeclared()));
    }

    private static double roundHalfUp(Double amountDeclared) {
        return Precision.round(amountDeclared, 2);
    }
}
