/*
 *  Copyright 2022 Selina Finance
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.selina.lending.internal.service.application.domain;

import java.util.Date;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Income {
    Double amount;
    String type;
    String status;
    String noIncomeSource;
    String docRequirements;
    Double amountVerified;
    Date incomeDate;
    String relatedYear;
    String frequency;
    Double contractDaysWorkedWeeklyReported;
    Double contractDayRateReported;
}
