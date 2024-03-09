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

package com.selina.lending.repository;

import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.exception.RemoteResourceProblemException;
import com.selina.lending.httpclient.eligibility.EligibilityApi;
import com.selina.lending.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EligibilityRepositoryTest extends MapperBase {

    @MockBean
    private TokenService tokenService;

    @MockBean
    private EligibilityApi eligibilityApi;

    @Autowired
    private EligibilityRepository eligibilityRepository;

    @Test
    void shouldRequestEligibilityService() {
        when(eligibilityApi.getEligibility(any())).thenReturn(getEligibilityResponse());

        var eligibility = eligibilityRepository.getEligibility(getQuickQuoteApplicationRequestDto(), List.of(getProduct()), true);

        assertThat(eligibility, equalTo(getEligibilityResponse()));
    }

    @Test
    void whenGetExceptionRequestingEligibilityServiceThenThrowRemoteResourceProblemException() {
        when(eligibilityApi.getEligibility(any())).thenThrow(RuntimeException.class);

        assertThrows(RemoteResourceProblemException.class, () -> eligibilityRepository.getEligibility(getQuickQuoteApplicationRequestDto(), List.of(getProduct()), true));
    }
}
