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

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.selina.lending.messaging.publisher.mapper.IPHelper.getRemoteAddr;
import static org.assertj.core.api.Assertions.assertThat;

class IPHelperTest {

    @Test
    public void shouldGetIpFromXForwardedHeaderWhenItPresentAndValid() {
        // Given
        var ip = "127.0.0.1";
        var httpRequest = new MockHttpServletRequest();
        httpRequest.addHeader("X-FORWARDED-FOR", ip);

        // When
        var result = getRemoteAddr(httpRequest);

        // Then
        assertThat(result).isEqualTo(ip);
    }

    @Test
    public void shouldNotGetIpFromXForwardedHeaderWhenItPresentAsEmptyString() {
        // Given
        var ip = "";
        var httpRequest = new MockHttpServletRequest();
        httpRequest.addHeader("X-FORWARDED-FOR", ip);

        // When
        var result = getRemoteAddr(httpRequest);

        // Then
        assertThat(result).isNotEqualTo(ip);
    }

    @Test
    public void shouldGetIpFromRequestRemoteAddrWhenXForwardedHeaderIsNull() {
        // Given
        var ip = "31.13.12.1";
        var httpRequest = new MockHttpServletRequest();
        httpRequest.setRemoteAddr(ip);

        // When
        var result = getRemoteAddr(httpRequest);

        // Then
        assertThat(result).isEqualTo(ip);
    }
}