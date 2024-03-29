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

package com.selina.lending.api.dto.dip.request;

public enum Tenure {
    LEASEHOLD("Leasehold"),
    FREEHOLD("Freehold"),
    LEASEHOLD_WITH_SHARE_OF_FREEHOLD("Leasehold with Share of Freehold"),
    COMMONHOLD("Commonhold");


    private final String value;
    Tenure(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
