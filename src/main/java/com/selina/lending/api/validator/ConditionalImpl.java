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

import static org.springframework.util.ObjectUtils.isEmpty;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConditionalImpl implements ConstraintValidator<Conditional, Object> {
    private String selected;
    private String[] required;
    private String message;
    private String[] values;


    @Override
    public void initialize(Conditional requiredIfChecked) {
        selected = requiredIfChecked.selected();
        required = requiredIfChecked.required();
        message = requiredIfChecked.message();
        values = requiredIfChecked.values();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        var propertyInvalidMap = new HashMap<>();
        boolean valid;
        try {
            var selectedValue = BeanUtils.getProperty(object, selected);
            if (Arrays.asList(values).contains(selectedValue)) {
                for (String propertyName : required) {
                    var requiredValue = BeanUtils.getProperty(object, propertyName);
                    valid = requiredValue != null && !isEmpty(requiredValue);
                    if (!valid) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(message).addPropertyNode(propertyName).addConstraintViolation();
                        propertyInvalidMap.put(propertyName, false);
                    }
                }
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error("An exception occurred while accessing class : {}, exception : {}", object.getClass().getName(), e);
            return false;
        }
        return propertyInvalidMap.isEmpty();
    }
}
