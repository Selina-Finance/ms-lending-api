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

package com.selina.lending.api.controller.swagger;

public class SwaggerConstants {
    public static final String INVALID_CREDENTIALS_EXAMPLE = "{\n" +
            "    \"title\": \"Bad Request\",\n" +
            "    \"status\": 400,\n" +
            "    \"detail\": \"Invalid client credentials\"\n" +
            "}";
    public static final String BAD_REQUEST_EXAMPLE =
            "{\n    \"title\": \"Constraint Violation\",\n    \"status\": 400,\n    \"violations\": [\n   {\n \"field\": \"field\",\n   \"message\": \"value is not valid\"\n }\n    ]\n }";
    public static final String ACCESS_DENIED_EXAMPLE =
            "{\n    \"title\": \"Error processing request\",\n    \"status\": 403,\n    \"detail\": \"Sorry, but you have no access to this resource\"\n}";
    public static final String NOT_FOUND_EXAMPLE =
            "{\n    \"title\": \"Not Found\",\n    \"status\": 404,\n    \"detail\": \"message details\"\n}";

    public static final String CONFLICT_EXAMPLE =
            "{\n    \"title\": \"Error processing request\",\n    \"status\": 409,\n    \"detail\": \"message details\"\n}";

    public static final String OFFER_SELECTED_EXAMPLE = "{\n   \"message\": \"Offer selected with success\",\n  \"externalApplicationId\": \"external id\",\n   \"productCode\": \"product code\"\n}";
    public static final String DATE_PATTERN = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
    public static final String DATE_INVALID_MESSAGE = "must match yyyy-MM-dd format";
    public static final String EXAMPLE_DATE = "2001-01-01";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String EMAIL_PATTERN = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";

    private SwaggerConstants() {}
}
