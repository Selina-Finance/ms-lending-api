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

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import java.util.Arrays;

@lombok.extern.slf4j.Slf4j
public class AtLeastOneNotBlankImpl implements ConstraintValidator<AtLeastOneNotBlank, Object> {

    private String[] fields;

    @Override
    public void initialize(AtLeastOneNotBlank atLeastOneNotBlank) {
        fields = atLeastOneNotBlank.fields();
    }

    @Override
    public boolean isValid(Object object, javax.validation.ConstraintValidatorContext context) {
        try {
            for (String field : fields) {
                if (StringUtils.hasText(BeanUtils.getProperty(object, field))) return true;
            }

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("At least one of these fields must be specified: " + Arrays.toString(fields)).addConstraintViolation();
        } catch (IllegalAccessException | NoSuchMethodException | java.lang.reflect.InvocationTargetException e) {
            log.error("An exception occurred while accessing class : {}, exception : {}", object.getClass().getName(), e);
            return false;
        }

        return false;
    }
}
