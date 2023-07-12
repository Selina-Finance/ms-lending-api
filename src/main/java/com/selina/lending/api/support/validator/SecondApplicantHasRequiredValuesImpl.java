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

import com.selina.lending.api.dto.dip.request.DIPApplicantDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Slf4j
public class SecondApplicantHasRequiredValuesImpl implements ConstraintValidator<SecondApplicantHasRequiredValues, List<DIPApplicantDto>> {

    private static final String[] REQUIRED_FIELDS = {"applicant2LivesWithApplicant1", "applicant2LivesWithApplicant1For3Years"};

    @Override
    public boolean isValid(List<DIPApplicantDto> applicants, ConstraintValidatorContext context) {
        if (hasSecondApplicant(applicants)) {
            var secondApplicant = applicants.get(1);
            var isValid = true;

            try {
                for (String field: REQUIRED_FIELDS) {
                    if (BeanUtils.getProperty(secondApplicant, field) == null) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(String.format("The field '%s' is required for the second applicant", field))
                                .addBeanNode()
                                .addConstraintViolation();
                        isValid = false;
                    }
                }

                return isValid;
            } catch (IllegalAccessException | NoSuchMethodException | java.lang.reflect.InvocationTargetException e) {
                log.error("An exception occurred while accessing class : {}, exception : {}", secondApplicant.getClass().getName(), e);
                return false;
            }
        }

        return true;
    }

    private boolean hasSecondApplicant(List<DIPApplicantDto> applicants) {
        return (applicants != null) && (applicants.size() == 2);
    }
}