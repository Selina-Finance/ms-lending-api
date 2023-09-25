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

package com.selina.lending.api.mapper.eligibility;

import com.selina.lending.api.dto.qq.request.QuickQuotePropertyDetailsDto;
import com.selina.lending.httpclient.eligibility.dto.request.EligibilityRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class EligibilityRequestMapper {

    @Mapping(target = "partnerAccountId", source = "partnerAccountId")
    @Mapping(target = "propertyDetails", source = "propertyDetails")
    public abstract EligibilityRequest mapToPropertyDetails(String partnerAccountId, QuickQuotePropertyDetailsDto propertyDetails);
}
