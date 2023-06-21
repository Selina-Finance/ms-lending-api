package com.selina.lending.api.support.validator;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UkNationalPhoneNumberImpl implements ConstraintValidator<UkNationalPhoneNumber, String> {

    private static final String REGION_GB = "GB";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        PhoneNumber phoneNumber;

        if (value == null){
            return true;
        }

        var phoneUtil = PhoneNumberUtil.getInstance();

        try {
           phoneNumber = phoneUtil.parse(value, REGION_GB);
        } catch (NumberParseException e) {
            return false;
        }

        return phoneUtil.isValidNumber(phoneNumber);
    }
}