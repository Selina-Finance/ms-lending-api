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

import com.selina.lending.internal.dto.DetailDto;

class CreditCommitmentsDetailDetailMapperTest extends MapperBase {

    @Test
    void mapToCreditCommitmentsDto() {
        //Given
        var creditCommitments = getCreditCommitments();

        //When
        var creditCommitmentsDto = CreditCommitmentsDetailMapper.INSTANCE.mapToCreditCommitmentsDto(creditCommitments);

        //Then
        assertDetails(creditCommitmentsDto.getCreditPolicy().getDetail().get(0));
        assertDetails(creditCommitmentsDto.getSystem().getDetail().get(0));
        assertDetails(creditCommitmentsDto.getVotersRoll().getDetail().get(0));
        assertDetails(creditCommitmentsDto.getPublicInformation().getSystem().getDetail().get(0));
        assertDetails(creditCommitmentsDto.getUser().getDetail().get(0));
  }

    private void assertDetails(DetailDto detailDto) {
        assertThat(detailDto.getId(), equalTo(DETAIL_ID));
        assertThat(detailDto.getStatus(), equalTo(STATUS));
    }
}
