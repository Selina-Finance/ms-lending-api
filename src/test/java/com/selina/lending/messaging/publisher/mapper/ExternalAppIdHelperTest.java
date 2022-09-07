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

package com.selina.lending.messaging.publisher.mapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.selina.lending.internal.mapper.MapperBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

import static com.selina.lending.messaging.publisher.mapper.ExternalAppIdHelper.getExternalAppId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExternalAppIdHelperTest extends MapperBase {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldFetchExternalAppIdWhenGetAppRequest() {
        // Given
        var externalAppId = UUID.randomUUID().toString();
        var path = "/application/" + externalAppId;
        var method = "GET";

        var httpRequest = mock(HttpServletRequest.class);
        when(httpRequest.getRequestURI()).thenReturn(path);
        when(httpRequest.getMethod()).thenReturn(method);

        // When
        var result = getExternalAppId(httpRequest);

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(externalAppId);
    }

    @Test
    public void shouldFetchExternalAppIdWhenUpdateAppRequest() {
        // Given
        var externalAppId = UUID.randomUUID().toString();
        var path = "/application/" + externalAppId + "/dip";
        var method = "PUT";

        var httpRequest = mock(HttpServletRequest.class);
        when(httpRequest.getRequestURI()).thenReturn(path);
        when(httpRequest.getMethod()).thenReturn(method);

        // When
        var result = getExternalAppId(httpRequest);

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(externalAppId);
    }

    @Test
    public void shouldFetchExternalAppIdWhenCreateAppRequest() throws IOException {
        // Given
        var application = getDIPApplicationRequestDto();
        var request = new MockHttpServletRequest(HttpMethod.POST.name(), "/application/dip");
        request.addHeader("content-type", "application/json");
        request.setContentType("application/json");
        request.setContent(objectMapper.writeValueAsBytes(application));

        // When
        var result = getExternalAppId(request);

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(application.getExternalApplicationId());
    }
}