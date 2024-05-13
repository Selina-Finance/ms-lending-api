package com.selina.lending.util;

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ABTestUtils {

    Logger log = LoggerFactory.getLogger(ABTestUtils.class);

    String TEST_GROUP_ID_DELIMITER = ", ";
    String GROUP_A = "Group A";
    String GROUP_B = "Group B";

    static void appendTestGroupId(QuickQuoteApplicationRequest request, String testGroupId) {
        var existingTestGroupId = request.getTestGroupId();

        if (existingTestGroupId != null) {
            request.setTestGroupId(existingTestGroupId.concat(TEST_GROUP_ID_DELIMITER).concat(testGroupId));
        } else {
            request.setTestGroupId(testGroupId);
        }
    }

    static boolean hasOddPrimaryApplicantBirthday(List<QuickQuoteApplicantDto> applicants) {
        try {
            var primaryApplicant = findPrimaryApplicant(applicants);
            return primaryApplicant.isPresent() && hasOddApplicantBirthday(primaryApplicant.get());
        } catch (Exception ex) {
            log.warn("Error checking if primary applicant birthday is odd! Return false!", ex);
            return false;
        }
    }

    private static Optional<QuickQuoteApplicantDto> findPrimaryApplicant(List<QuickQuoteApplicantDto> applicants) {
        return applicants.stream()
                .filter(applicant -> applicant.getPrimaryApplicant() != null && applicant.getPrimaryApplicant())
                .findFirst();
    }

    private static boolean hasOddApplicantBirthday(QuickQuoteApplicantDto applicant) {
        var birthday = LocalDate.parse(applicant.getDateOfBirth());
        return birthday.getDayOfMonth() % 2 != 0;
    }
}
