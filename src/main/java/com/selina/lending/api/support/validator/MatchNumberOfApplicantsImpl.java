package com.selina.lending.api.support.validator;

import com.selina.lending.internal.dto.ApplicationRequest;
import com.selina.lending.api.dto.dip.request.DIPApplicationRequest;
import com.selina.lending.internal.dto.DIPCCApplicationRequest;
import com.selina.lending.internal.dto.LoanInformationDto;
import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.dto.quotecf.QuickQuoteCFApplicationRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

public class MatchNumberOfApplicantsImpl implements ConstraintValidator<MatchNumberOfApplicants, ApplicationRequest> {

    private static final String PROPERTY_NODE = "loanInformation.numberOfApplicants";

    @Override
    public boolean isValid(ApplicationRequest value, ConstraintValidatorContext context) {
        if (value == null){
            return true;
        }
        List<?> applicants = null;
        Optional<Integer> numberOfApplicants = Optional.empty();
        if (value instanceof QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
            applicants = quickQuoteApplicationRequest.getApplicants();
            numberOfApplicants = Optional.ofNullable(quickQuoteApplicationRequest.getLoanInformation())
                    .map(LoanInformationDto::getNumberOfApplicants);
        } else if (value instanceof QuickQuoteCFApplicationRequest quickQuoteCFApplicationRequest) {
            applicants = quickQuoteCFApplicationRequest.getApplicants();
            numberOfApplicants = Optional.ofNullable(quickQuoteCFApplicationRequest.getLoanInformation())
                    .map(LoanInformationDto::getNumberOfApplicants);
        } else if (value instanceof DIPApplicationRequest dipApplicationRequest) {
            applicants = dipApplicationRequest.getApplicants();
            numberOfApplicants = Optional.ofNullable(dipApplicationRequest.getLoanInformation())
                    .map(LoanInformationDto::getNumberOfApplicants);
        } else if (value instanceof DIPCCApplicationRequest dipccApplicationRequest) {
            applicants = dipccApplicationRequest.getApplicants();
            numberOfApplicants = Optional.ofNullable(dipccApplicationRequest.getLoanInformation())
                    .map(LoanInformationDto::getNumberOfApplicants);
        }

        if (applicants == null || numberOfApplicants.isEmpty()) {
            return true;
        }

        boolean isValid = (applicants.size() == numberOfApplicants.get());

        if (!isValid) {
            createCustomMessage(context);
        }

        return isValid;
    }

    private void createCustomMessage(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode(PROPERTY_NODE)
                .addConstraintViolation();
    }

}
