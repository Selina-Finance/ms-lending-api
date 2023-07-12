package com.selina.lending.api.validator;


import com.selina.lending.api.dto.common.AddressDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MoveOutAfterMoveInDateImpl implements ConstraintValidator<MoveOutAfterMoveInDate, AddressDto> {

    private static final String ADDRESS_TYPE_PREVIOUS = "previous";

    @Override
    public boolean isValid(AddressDto value, ConstraintValidatorContext context) {
        if (!ADDRESS_TYPE_PREVIOUS.equals(value.getAddressType())) {
            return true;
        }

        if (value.getToDate() == null || value.getFromDate() == null) {
            return true;
        }

        return value.getToDate().isAfter(value.getFromDate());
    }

}
