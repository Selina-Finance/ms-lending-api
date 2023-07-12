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

package com.selina.lending.api.mapper.applicant;

import com.selina.lending.api.dto.application.response.CreditCheckDto;
import com.selina.lending.httpclient.middleware.dto.common.CreditCheck;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreditCheckMapper {
    CreditCheckMapper INSTANCE = Mappers.getMapper(CreditCheckMapper.class);

    CreditCheckDto mapToCreditCheckDto(CreditCheck creditCheck);
}
