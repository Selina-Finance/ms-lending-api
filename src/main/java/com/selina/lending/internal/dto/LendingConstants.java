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

    public static final String SOURCE_ACCOUNT_JWT_CLAIM_NAME = "sourceAccount";

    public static final String CLIENT_ID_JWT_CLAIM_NAME = "clientId";

    public static final String PARTNER_ACCOUNT_ID_JWT_CLAIM_NAME = "partnerAccountId";

    public static final String PRODUCT_CODE_ALL = "All";

    public static final String REQUEST_SOURCE = Source.LENDING_API.toString();

    public static final String STAGE_OVERWRITE = "DIP - Credit Commitments";

    public static final String REFER_DECISION = "Refer";

    public static final String ACCEPT_DECISION = "Accept";

    public static final String DECLINE_DECISION = "Decline";

    public static final String DIP_APPLICATION_TYPE = "DIP";

    public static final String SOURCE_TYPE_JWT_CLAIM_NAME = "sourceType";

    public static final String ARRANGEMENT_FEE_DISCOUNT_SELINA_JWT_CLAIM_NAME = "arrangementFeeDiscountSelina";

    private LendingConstants() {

    }
}
