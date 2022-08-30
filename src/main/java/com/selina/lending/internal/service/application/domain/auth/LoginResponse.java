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

package com.selina.lending.internal.service.application.domain.auth;

public record LoginResponse(
        String access_token,
        Integer expires_in
){}
//{
//        "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJiS2dlQWxnaURucjc0RXR6TDJQUWtlVkkyWU5TMmV5NlNvRVZVQmhHVUl3In0.eyJleHAiOjE2NjE1MTIzMTYsImlhdCI6MTY2MTUxMjAxNiwianRpIjoiZDdmOTRmMDktYmM5Yi00YzE3LTk1OWUtNjE5Y2JkOTcxZWEyIiwiaXNzIjoiaHR0cHM6Ly9zc28tc3RhZ2luZy5zZWxpbmFkZXYuY28udWsvYXV0aC9yZWFsbXMvYnJva2VyLWNsaWVudCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJiYjQwNzkzZi1kNTVlLTRiYTYtYmZiYS01MWNlYjhiOTMzNjgiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0aGUtYnJva2VyIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtYnJva2VyLWNsaWVudCIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJjbGllbnRJZCI6InRoZS1icm9rZXIiLCJjbGllbnRIb3N0IjoiMzUuMTc3LjUuNzMiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtdGhlLWJyb2tlciIsImNsaWVudEFkZHJlc3MiOiIzNS4xNzcuNS43MyJ9.LGSzd4zxrF0AXSW4kH0gaualAs_6qjh5gqk5jiSu7K6A59j55obo5IEiG-FYDG19f9ZA2yqdVOr5vksww5vQQTCvnntpmWQ3sue6b01P-ktDeQ1mzXwP9M0KDcXjUkhyvXDNuggC2z5smblMhCJxmnxx6aQjFvira4KL1HyCTPjDeyWMba4y3PO34Oyt-yPAq90oEzFMk1EfK3ZX2Puth1rJxD93JoVjl89v2oEdQJlxbCNcvcCWsmXByWc9L-9ffGPeJ_v75-ONkaqf3FNJK3iiQ4Ul-k1odeG5KL9jqwzdg9JUPHP-44rBtinjT0rVwDhdsEV58R4xb_LosxfySA",
//        "expires_in": 300,
//        "refresh_expires_in": 0,
//        "token_type": "Bearer",
//        "not-before-policy": 0,
//        "scope": "email profile"
//        }