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

public enum LoanPurpose {
    DEBT_CONSOLIDATION("Debt consolidation"),
    HOME_IMPROVEMENTS("Home improvements"),
    HOLIDAY("Holiday"),
    MEDICAL_BILLS("Medical bills"),
    PROPERTY_SECOND_HOME_PURCHASE("Property - Second home purchase"),
    PROPERTY_BUY_TO_LET_PURCHASE("Property - Buy-to-let purchase"),
    SCHOOL_FEES_FURTHER_EDUCATION("School fees / further education"),
    TAX_BILLS("Tax bills"),
    VEHICLE_PURCHASE("Vehicle purchase"),
    WEDDING("Wedding"),
    BUSINESS_PURPOSES("Business purposes"),
    CONTINGENCY("Contingency"),
    OTHER("Other"),
    NONE("None");

    final String value;

    LoanPurpose(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
