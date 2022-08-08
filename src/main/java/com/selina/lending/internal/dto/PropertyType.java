/*
 *   Copyright 2022 Selina Finance
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package com.selina.lending.internal.dto;

public enum PropertyType {
    DETACHED_BUNGALOW("Detached bungalow"),
    SEMI_DETACHED_BUNGALOW("Semi-detached bungalow"),
    TERRACED_BUNGALOW("Terraced bungalow"),
    DETACHED_HOUSE("Detached house"),
    SEMI_DETACHED_HOUSE("Semi-detached house"),
    TERRACED_HOUSE("Terraced house"),
    FLAT_MAISONETTE_IN_CONVERTED_HOUSE("Flat or maisonette in converted house"),
    PURPOSE_BUILD_FLAT_OR_MAISONETTE("Purpose-built flat or maisonette"),
    OTHER("Other"),
    STUDIO_FLAT("Studio flat");

    final String value;
    PropertyType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
