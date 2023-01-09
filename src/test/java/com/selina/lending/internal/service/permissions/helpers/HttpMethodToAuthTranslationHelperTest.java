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

package com.selina.lending.internal.service.permissions.helpers;

import org.junit.jupiter.api.Test;

import static com.selina.lending.internal.service.permissions.helpers.HttpMethodToAuthTranslationHelper.toAuthScope;
import static org.assertj.core.api.Assertions.assertThat;

class HttpMethodToAuthTranslationHelperTest {

    @Test
    void shouldTranslateToWriteWhenPost() {
        // Given
        var httpMethod = "POST";
        String expected = "Write";

        // When
        var result = toAuthScope(httpMethod);

        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldTranslateToWriteWhenPut() {
        // Given
        var httpMethod = "PUT";
        String expected = "Write";

        // When
        var result = toAuthScope(httpMethod);

        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldTranslateToWriteWhenPatch() {
        // Given
        var httpMethod = "PATCH";
        String expected = "Write";

        // When
        var result = toAuthScope(httpMethod);

        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldTranslateToWriteWhenDelete() {
        // Given
        var httpMethod = "DELETE";
        String expected = "Write";

        // When
        var result = toAuthScope(httpMethod);

        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldTranslateToReadWhenGet() {
        // Given
        var httpMethod = "GET";
        String expected = "Read";

        // When
        var result = toAuthScope(httpMethod);

        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldThrowTranslateToReadWhenGet() {
        // Given
        var httpMethod = "OPTIONS";
        String expected = "Read";

        // When
        var result = toAuthScope(httpMethod);

        // Then
        assertThat(result).isEqualTo(expected);
    }
}