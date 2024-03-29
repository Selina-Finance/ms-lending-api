package com.selina.lending.api.validator;

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class MaximumOnePrimaryApplicantImpl implements ConstraintValidator<MaximumOnePrimaryApplicant, List<QuickQuoteApplicantDto>> {

    @Override
    public boolean isValid(List<QuickQuoteApplicantDto> list, ConstraintValidatorContext context) {
        if (list == null) {
            return true;
        }

        if (list.size() == 1) {
            return validateOneApplicant(list.get(0));
        }

        return validateApplicantList(list);
    }

    private boolean validateOneApplicant(QuickQuoteApplicantDto quickQuoteApplicantDto) {
        return quickQuoteApplicantDto != null &&
                (quickQuoteApplicantDto.getPrimaryApplicant() == null || quickQuoteApplicantDto.getPrimaryApplicant());
    }

    private boolean validateApplicantList(List<QuickQuoteApplicantDto> list) {
        if (hasOnlyOnePrimaryApplicant(list)){
            return true;
        }
        return doesAllApplicantsHavePrimaryApplicantNull(list);
    }

    private boolean hasOnlyOnePrimaryApplicant(List<QuickQuoteApplicantDto> list){
        return list.stream().filter(quickQuoteApplicantDto -> quickQuoteApplicantDto != null
                        && quickQuoteApplicantDto.getPrimaryApplicant() != null && quickQuoteApplicantDto.getPrimaryApplicant())
                .count() == 1;
    }

    private boolean doesAllApplicantsHavePrimaryApplicantNull(List<QuickQuoteApplicantDto> list) {
        return list.stream().allMatch(quickQuoteApplicantDto -> quickQuoteApplicantDto == null ||
                quickQuoteApplicantDto.getPrimaryApplicant() == null);
    }

}
