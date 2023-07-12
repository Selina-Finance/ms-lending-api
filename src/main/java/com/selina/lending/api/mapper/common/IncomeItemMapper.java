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

import com.selina.lending.api.dto.common.IncomeItemDto;
import com.selina.lending.httpclient.middleware.dto.common.Income;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IncomeItemMapper {
    IncomeItemMapper INSTANCE = Mappers.getMapper(IncomeItemMapper.class);

    IncomeItemDto mapToIncomeItemDto(Income income);
    Income mapToIncome(IncomeItemDto incomeItemDto);

}
