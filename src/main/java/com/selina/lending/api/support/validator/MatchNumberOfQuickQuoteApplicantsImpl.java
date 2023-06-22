package com.selina.lending.api.support.validator;

import com.selina.lending.internal.dto.LoanInformationDto;
import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

public class MatchNumberOfQuickQuoteApplicantsImpl implements ConstraintValidator<MatchNumberOfQuickQuoteApplicants, QuickQuoteApplicationRequest> {

    @Override
    public boolean isValid(QuickQuoteApplicationRequest value, ConstraintValidatorContext context) {
        if (value == null){
            return true;
        }

        int applicantsSize = getApplicantsSize(value);
        int numberOfApplicants = getNumberOfApplicants(value, applicantsSize);
        boolean isValid = ( applicantsSize == numberOfApplicants);

        if (!isValid) {
            createCustomMessage(context);
        }

        return isValid;
    }

    private int getApplicantsSize(QuickQuoteApplicationRequest application){
        return Optional.ofNullable(application.getApplicants())
                .map(List::size)
                .orElse(0);
    }

    private int getNumberOfApplicants(QuickQuoteApplicationRequest application, int applicantsSize){
        return Optional.ofNullable(application.getLoanInformation())
                .map(LoanInformationDto::getNumberOfApplicants)
                .orElse(applicantsSize);
    }

    private void createCustomMessage(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("loanInformation.numberOfApplicants")
                .addConstraintViolation();
    }

}
