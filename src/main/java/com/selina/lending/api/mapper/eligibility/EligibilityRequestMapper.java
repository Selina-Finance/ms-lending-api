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

package com.selina.lending.api.mapper.eligibility;

import com.selina.lending.api.dto.common.IncomeDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.httpclient.eligibility.dto.request.Applicant;
import com.selina.lending.httpclient.eligibility.dto.request.CreditRisk;
import com.selina.lending.httpclient.eligibility.dto.request.EligibilityRequest;
import com.selina.lending.httpclient.eligibility.dto.request.Income;
import com.selina.lending.httpclient.quickquote.Product;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class EligibilityRequestMapper {

    @Mapping(target = "partnerAccountId", source = "partnerAccountId")
    @Mapping(target = "propertyDetails", source = "request.propertyDetails")
    @Mapping(target = "applicant", source = "request.applicants", qualifiedByName = "mapApplicant")
    public abstract EligibilityRequest mapToPropertyDetails(String partnerAccountId, QuickQuoteApplicationRequest request, @Context List<Product> products);

    @Named("mapApplicant")
    Applicant mapApplicant(List<QuickQuoteApplicantDto> applicants, @Context List<Product> products) {
        var primaryApplicant = findPrimaryApplicant(applicants);
        if (primaryApplicant == null) {
            return null;
        }

        return Applicant.builder()
                .incomes(mapIncomes(primaryApplicant.getIncome()))
                .creditRisk(mapCreditRisk(primaryApplicant, products))
                .build();
    }

    private QuickQuoteApplicantDto findPrimaryApplicant(List<QuickQuoteApplicantDto> applicants) {
        return applicants.stream()
                .filter(applicant -> applicant.getPrimaryApplicant() != null && applicant.getPrimaryApplicant())
                .findFirst()
                .orElse(null);
    }

    private List<Income> mapIncomes(IncomeDto incomeDto) {
        var incomes = new ArrayList<Income>();

        if (incomeDto != null && incomeDto.getIncome() != null) {
            incomeDto.getIncome().forEach(income -> incomes.add(Income.builder()
                    .amount(income.getAmount())
                    .type(income.getType())
                    .build()));
        }

        return incomes;
    }

    private CreditRisk mapCreditRisk(QuickQuoteApplicantDto applicant, List<Product> products) {
        return CreditRisk.builder()
                .filterPassed(applicant.getFilterPassed())
                .ltv(mapLtv(products))
                .lti(mapLti(products))
                .dti(mapDti(products))
                .build();
    }

    private Double mapLtv(List<Product> products) {
        return products.stream()
                .findFirst()
                .map(product -> product.getOffer().getNetLtv())
                .orElse(null);
    }

    private Double mapLti(List<Product> products) {
        return products.stream()
                .map(product -> product.getOffer().getLti())
                .max(Double::compareTo)
                .orElse(null);
    }

    private Double mapDti(List<Product> products) {
        return products.stream()
                .map(product -> product.getOffer().getDtir())
                .max(Double::compareTo)
                .orElse(null);
    }
}
