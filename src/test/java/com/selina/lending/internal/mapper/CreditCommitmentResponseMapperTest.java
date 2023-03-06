/*
 * Copyright 2022 Selina Finance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.selina.lending.internal.mapper;

import com.selina.lending.internal.dto.creditcommitments.response.DetailResponseDto;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class CreditCommitmentResponseMapperTest extends MapperBase {

    @Test
    void mapToCreditCommitmentResponse() {
        //Given
        var response = getCreditCommitmentResponse();

        //When
        var mappedResponse = CreditCommitmentResponseMapper.INSTANCE.mapToCreditCommitmentResponse(response);

        //Then
        assertThat(mappedResponse.getCreditCommitment().getApplicants().size(),
                equalTo(getCreditCommitmentResponse().getCreditCommitment().getApplicants().size()));

        var applicant = mappedResponse.getCreditCommitment().getApplicants().get(0);
        assertThat(applicant.getPrimaryApplicant(), equalTo(true));
        assertThat(applicant.getCreditScore(), equalTo(CREDIT_SCORE));

        var creditCommitments = applicant.getCreditCommitments();
        assertDetails(creditCommitments.getSystem().getDetail().get(0));
        assertDetails(creditCommitments.getUser().getDetail().get(0));

        var publicInfo = applicant.getPublicInformation();
        assertDetails(publicInfo.getSystem().getDetail().get(0));
        assertDetails(publicInfo.getUser().getDetail().get(0));
    }

    private void assertDetails(DetailResponseDto detail) {
        assertThat(detail.getId(), equalTo(DETAIL_ID));
        assertThat(detail.getStatus(), equalTo(STATUS));
    }
}
