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

import com.selina.lending.api.dto.creditcommitments.request.ApplicantCreditCommitmentsDto;
import com.selina.lending.api.dto.dip.request.DIPApplicantDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class OnlyOnePrimaryApplicantImpl implements ConstraintValidator<OnlyOnePrimaryApplicant, List<?>> {

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext constraintValidatorContext) {
        if (list != null) {
            Object obj = list.get(0);
            if (obj instanceof DIPApplicantDto) {
                return list.stream().map(DIPApplicantDto.class::cast)
                        .filter(applicant -> applicant.getPrimaryApplicant() != null)
                        .filter(DIPApplicantDto::getPrimaryApplicant).count() == 1;
            } else {
                return list.stream().map(ApplicantCreditCommitmentsDto.class::cast)
                        .filter(applicant -> applicant.getPrimaryApplicant() != null)
                        .filter(ApplicantCreditCommitmentsDto::getPrimaryApplicant).count() == 1;
            }
        }
        return false;
    }
}