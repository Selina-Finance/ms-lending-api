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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import com.selina.lending.internal.service.application.domain.Detail;

class UpdateCreditCommitmentsRequestMapperTest extends MapperBase {

    @Test
    void mapToUpdateCreditCommitmentsRequest() {
        //Given
        var request = getUpdateCreditCommitmentsRequest();

        //When
        var updateRequest = UpdateCreditCommitmentsRequestMapper.INSTANCE.mapToUpdateCreditCommitmentsRequest(request);

        //Then
        assertThat(updateRequest.getApplicants()).hasSameSizeAs(request.getApplicants());

        var appCreditCommitment = updateRequest.getApplicants().get(0);
        assertThat(appCreditCommitment.getCreditScore()).isEqualTo(CREDIT_SCORE);
        assertThat(appCreditCommitment.getPrimaryApplicant()).isTrue();
        assertDetails(appCreditCommitment.getCreditCommitments().getCreditPolicy().getDetail().get(0));
        assertDetails(appCreditCommitment.getCreditCommitments().getUser().getDetail().get(0));
        assertDetails(appCreditCommitment.getCreditCommitments().getPublicInformation().getSystem().getDetail().get(0));
        assertDetails(appCreditCommitment.getCreditCommitments().getVotersRoll().getDetail().get(0));
        assertDetails(appCreditCommitment.getCreditCommitments().getSystem().getDetail().get(0));
    }

    private void assertDetails(Detail detail) {
        MatcherAssert.assertThat(detail.getId(), equalTo(DETAIL_ID));
        MatcherAssert.assertThat(detail.getStatus(), equalTo(STATUS));
    }
}