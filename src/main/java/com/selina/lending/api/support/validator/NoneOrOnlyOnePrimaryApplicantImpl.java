package com.selina.lending.api.support.validator;

import com.selina.lending.internal.dto.quote.QuickQuoteApplicantDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NoneOrOnlyOnePrimaryApplicantImpl implements ConstraintValidator<NoneOrOnlyOnePrimaryApplicant, List<QuickQuoteApplicantDto>> {

    @Override
    public boolean isValid(List<QuickQuoteApplicantDto> list, ConstraintValidatorContext context) {
        if (list == null){
            return false;
        }
        if (list.size() == 1){
            return validateOneApplicant(list.get(0));
        }
        return validateApplicantList(list);
    }

    private boolean validateOneApplicant(QuickQuoteApplicantDto quickQuoteApplicantDto) {
        return quickQuoteApplicantDto != null &&
                (quickQuoteApplicantDto.getPrimaryApplicant() == null || quickQuoteApplicantDto.getPrimaryApplicant());
    }

    private boolean validateApplicantList(List<QuickQuoteApplicantDto> list) {
        if (haveOnlyOnePrimaryApplicant(list)){
            return true;
        }
        return isAllApplicantPrimaryApplicantNull(list);
    }

    private boolean haveOnlyOnePrimaryApplicant(List<QuickQuoteApplicantDto> list){
        return list.stream().filter(quickQuoteApplicantDto -> quickQuoteApplicantDto != null
                        && quickQuoteApplicantDto.getPrimaryApplicant() != null && quickQuoteApplicantDto.getPrimaryApplicant())
                .count() == 1;
    }

    private boolean isAllApplicantPrimaryApplicantNull(List<QuickQuoteApplicantDto> list) {
        return list.stream().allMatch(quickQuoteApplicantDto -> quickQuoteApplicantDto != null &&
                quickQuoteApplicantDto.getPrimaryApplicant() == null);
    }

}
