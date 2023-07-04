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

package com.selina.lending.api.support.validator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class LessThanOrEqualToImpl implements ConstraintValidator<LessThanOrEqualTo, Object> {

    private static final String ERROR_MESSAGE_FORMAT = "The '%s' must be less than or equal to the '%s'";

    private String comparisonValueFieldName;
    private String valueToCompareToFieldName;
    private String errorMessage;

    @Override
    public void initialize(LessThanOrEqualTo lessThanOrEqualTo) {
        comparisonValueFieldName = lessThanOrEqualTo.comparisonValueFieldName();
        valueToCompareToFieldName = lessThanOrEqualTo.valueToCompareToFieldName();
        errorMessage = ERROR_MESSAGE_FORMAT.formatted(comparisonValueFieldName, valueToCompareToFieldName);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            boolean isValid;
            var comparisonValue = BeanUtils.getProperty(object, comparisonValueFieldName);
            var valueToCompareTo = BeanUtils.getProperty(object, valueToCompareToFieldName);

            if (comparisonValue == null || valueToCompareTo == null)
                return true;

            isValid = Double.compare(Double.parseDouble(comparisonValue), Double.parseDouble(valueToCompareTo)) <= 0;

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(errorMessage).addPropertyNode(comparisonValueFieldName).addConstraintViolation();
            }

            return isValid;
        } catch (Exception ex) {
            log.error("Unexpected validation error class={}", object.getClass().getName(), ex);
            return false;
        }
    }
}
