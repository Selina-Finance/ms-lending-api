package com.selina.lending.api.support.validator;


import com.selina.lending.internal.dto.AddressDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MoveOutAfterMoveInDateImpl implements ConstraintValidator<MoveOutAfterMoveInDate, AddressDto> {

    @Override
    public boolean isValid(AddressDto value, ConstraintValidatorContext context) {
        if (!value.getAddressType().equals("previous")) {
            return true;
        }

        if (value.getToDate() == null || value.getFromDate() == null) {
            return true;
        }

        return value.getToDate().isAfter(value.getFromDate());

    }

}
