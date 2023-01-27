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

package com.selina.lending.internal.service.application.domain;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApplicationRequest {
    private String applicationType;
    private Boolean includeCreditCommitment;
    private String source;
    private String sourceClientId;
    private String sourceAccount;
    private String productCode;
    private String reference;
    private String selectedOffer;
    private String selectedProduct;
    private String externalApplicationId;
    private Boolean runDecisioning;
    private LoanInformation loanInformation;
    private PropertyDetails propertyDetails;
    private Fees fees;
    private List<Applicant> applicants;
    private List<Expenditure> expenditure;
    private String stageOverwrite;
}
