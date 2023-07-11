package com.selina.lending.internal.mapper.quotecf;

import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.httpclient.middleware.dto.common.Applicant;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class QuickQuoteCFApplicantMapperTest extends MapperBase {


    @Test
    void mapToApplicantFromQuickQuoteCFApplicantDto() {
        //Given
        var applicantDto = getQuickQuoteCFApplicantDto();

        //When
        Applicant applicant = QuickQuoteCFApplicantMapper.INSTANCE.mapToApplicant(applicantDto);

        //Then
        assertThat(applicant.getTitle(), equalTo(TITLE));
        assertThat(applicant.getFirstName(), equalTo(FIRST_NAME));
        assertThat(applicant.getLastName(), equalTo(LAST_NAME));
        assertThat(applicant.getGender(), equalTo(GENDER));
        assertThat(applicant.getEmailAddress(), equalTo(EMAIL_ADDRESS));
        assertThat(applicant.getMobilePhoneNumber(), equalTo(MOBILE_NUMBER));
        assertThat(applicant.getDateOfBirth(), equalTo(DOB));
        assertThat(applicant.getAddresses().size(), equalTo(1));
        assertThat(applicant.getAddresses().get(0).getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(applicant.getIncome().getIncome().size(), equalTo(1));
        assertThat(applicant.getIncome().getIncome().get(0).getAmount(), equalTo(INCOME_AMOUNT));
        assertThat(applicant.getIncome().getIncome().get(0).getType(), equalTo(INCOME_TYPE));
        assertThat(applicant.getEmployment().getEmploymentStatus(), equalTo(EMPLOYED_STATUS));
        assertThat(applicant.getEmployment().getEmployerName(), equalTo(EMPLOYER_NAME));
        assertFalse(applicant.getEmployment().getInProbationPeriod());
        assertThat(applicant.getResidentialStatus(), equalTo(RESIDENTIAL_STATUS_OWNER));
    }


}
