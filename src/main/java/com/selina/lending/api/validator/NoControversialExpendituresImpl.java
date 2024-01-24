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

package com.selina.lending.api.validator;

import com.selina.lending.api.dto.common.ExpenditureDto;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

@Slf4j
public class NoControversialExpendituresImpl implements ConstraintValidator<NoControversialExpenditures, List<ExpenditureDto>> {

    @Override
    public boolean isValid(List<ExpenditureDto> expenditures, ConstraintValidatorContext context) {
        if (expenditures == null) return true;

        var isValid = true;

        try {
            Map<String, Set<String>> expendituresFrequenciesPerTypes = expenditures.stream().collect(
                    groupingBy(
                            ExpenditureDto::getExpenditureType,
                            mapping(ExpenditureDto::getFrequency, toSet())
                    )
            );

            isValid = expendituresFrequenciesPerTypes.values().stream().noneMatch(frequencies -> frequencies.size() > 1);
        } catch (Exception ex) {
            log.error("Unexpected NoControversialExpenditures validation error. Consider the validation passed!", ex);
        }

        return isValid;
    }
}