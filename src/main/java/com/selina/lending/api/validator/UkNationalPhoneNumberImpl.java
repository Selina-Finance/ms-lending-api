package com.selina.lending.api.validator;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UkNationalPhoneNumberImpl implements ConstraintValidator<UkNationalPhoneNumber, String> {

    private static final String REGION_GB = "GB";
    private static final String REGEX = "^\\+?[\\d\\s]+$";

    private final PhoneNumberUtil phoneNumberUtil;

    public UkNationalPhoneNumberImpl() {
        this.phoneNumberUtil = PhoneNumberUtil.getInstance();
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        if (!value.matches(REGEX)) {
            return false;
        }

        PhoneNumber phoneNumber;

        try {
            phoneNumber = phoneNumberUtil.parse(value, REGION_GB);
        } catch (NumberParseException e) {
            return false;
        }

        return phoneNumberUtil.isValidNumberForRegion(phoneNumber, REGION_GB);
    }
}