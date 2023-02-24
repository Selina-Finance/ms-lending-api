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

package com.selina.lending.internal.mapper.quote;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.selina.lending.internal.dto.quote.ProductOfferDto;
import com.selina.lending.internal.dto.quote.QuickQuoteResponse;
import com.selina.lending.internal.service.application.domain.quote.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.internal.service.application.domain.quote.Product;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuickQuoteApplicationResponseMapper {
    QuickQuoteApplicationResponseMapper INSTANCE = Mappers.getMapper(QuickQuoteApplicationResponseMapper.class);

    @Mapping(source = "products", target = "offers")
    @Mapping(source = "decision", target = "status")
    QuickQuoteResponse mapToQuickQuoteResponse(FilteredQuickQuoteDecisionResponse filteredQuickQuoteDecisionResponse);

    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.code", target = "code")
    @Mapping(source = "product.isVariable", target = "isVariable")
    @Mapping(source = "product.hasErc", target = "hasErc")
    @Mapping(source = "product.ercShortCode", target = "ercShortCode")
    @Mapping(source = "offer.id", target = "id")
    @Mapping(source = "offer.hasFee", target = "hasFee")
    @Mapping(source = "offer.productFee", target = "productFee")
    @Mapping(source = "offer.aprc", target = "aprc")
    @Mapping(source = "offer.ear", target = "ear")
    @Mapping(source = "offer.svr", target = "svr")
    @Mapping(source = "offer.initialRate", target = "initialRate")
    @Mapping(source = "offer.initialPayment", target = "initialPayment")
    @Mapping(source = "offer.initialTerm", target = "initialTerm")
    @Mapping(source = "offer.term", target = "term")
    @Mapping(source = "offer.reversionTerm", target = "reversionTerm")
    @Mapping(source = "offer.totalAmountRepaid", target = "totalAmountRepaid")
    @Mapping(source = "offer.maximumLoanAmount", target = "maximumLoanAmount")
    @Mapping(source = "offer.offerBalance", target = "offerBalance")
    @Mapping(source = "offer.requestedLoanAmount", target = "requestedLoanAmount")
    @Mapping(source = "offer.hasProductFeeAddedToLoan", target = "hasProductFeeAddedToLoan")
    @Mapping(source = "offer.ltvCap", target = "ltvCap")
    @Mapping(source = "offer.decision", target = "decision")
    @Mapping(source = "offer.ercPeriodYears", target = "ercPeriodYears")
    @Mapping(source = "offer.ercData", target = "ercData")
    ProductOfferDto mapToProductOfferDto(Product product);
}
