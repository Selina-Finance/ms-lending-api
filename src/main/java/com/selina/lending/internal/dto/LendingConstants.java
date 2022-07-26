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

package com.selina.lending.internal.dto;

public final class LendingConstants {
    public static final String DATE_PATTERN = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
    public static final String DATE_INVALID_MESSAGE = "must match yyyy-MM-dd format";

    public static final String SOURCE_ACCOUNT_JWT_CLAIM_NAME = "sourceAccount";

    public static final String CLIENT_ID_JWT_CLAIM_NAME = "clientId";

    public static final String PRODUCT_CODE_ALL = "All";

    public static final String REQUEST_SOURCE =  Source.BROKER.toString();

    public static final String STAGE_OVERWRITE =  "Dip - Credit Commitments";

    public static final String EXAMPLE_DATE = "2001-01-01";

    private LendingConstants() {

    }
}
