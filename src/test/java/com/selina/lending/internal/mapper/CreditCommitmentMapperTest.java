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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

class CreditCommitmentMapperTest extends MapperBase {

    @Test
    void mapToCreditCommitmentsDto() {
        //Given
        var creditCommitments = getCreditCommitments();

        //When
        var creditCommitmentsDto = com.selina.lending.internal.mapper.CreditCommitmentMapper.INSTANCE.mapToCreditCommitmentsDto(creditCommitments);

        //Then
        assertThat(creditCommitmentsDto.getCreditPolicy().getDetail().get(0).getId(), equalTo(DETAIL_ID));
        assertThat(creditCommitmentsDto.getCreditPolicy().getDetail().get(0).getAccountNumber(), equalTo(DETAIL_ACCOUNT_NUMBER));
        assertThat(creditCommitmentsDto.getSystem().getDetail().get(0).getId(), equalTo(DETAIL_ID));
        assertThat(creditCommitmentsDto.getSystem().getDetail().get(0).getAccountNumber(), equalTo(DETAIL_ACCOUNT_NUMBER));
        assertThat(creditCommitmentsDto.getVotersRoll().getDetail().get(0).getId(), equalTo(DETAIL_ID));
        assertThat(creditCommitmentsDto.getVotersRoll().getDetail().get(0).getAccountNumber(), equalTo(DETAIL_ACCOUNT_NUMBER));
        assertThat(creditCommitmentsDto.getPublicInformation().getSystem().getDetail().get(0).getId(), equalTo(DETAIL_ID));
        assertThat(creditCommitmentsDto.getPublicInformation().getSystem().getDetail().get(0).getAccountNumber(), equalTo(DETAIL_ACCOUNT_NUMBER));
        assertThat(creditCommitmentsDto.getUser().getDetail().get(0).getId(), equalTo(DETAIL_ID));
        assertThat(creditCommitmentsDto.getUser().getDetail().get(0).getAccountNumber(), equalTo(DETAIL_ACCOUNT_NUMBER));
    }
}
