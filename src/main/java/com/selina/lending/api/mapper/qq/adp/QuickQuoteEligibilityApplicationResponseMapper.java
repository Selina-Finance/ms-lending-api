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

package com.selina.lending.api.mapper.qq.adp;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.selina.lending.api.dto.qq.response.ProductOfferDto;
import com.selina.lending.api.dto.qq.response.QuickQuoteResponse;
import com.selina.lending.httpclient.adp.dto.response.QuickQuoteEligibilityDecisionResponse;
import com.selina.lending.httpclient.adp.dto.response.Product;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuickQuoteEligibilityApplicationResponseMapper {
    QuickQuoteEligibilityApplicationResponseMapper INSTANCE = Mappers.getMapper(
            QuickQuoteEligibilityApplicationResponseMapper.class);

    @Mapping(source = "products", target = "offers")
    @Mapping(source = "decision", target = "status")
    QuickQuoteResponse mapToQuickQuoteResponse(QuickQuoteEligibilityDecisionResponse quickQuoteDecisionResponse);

    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.code", target = "code")
    @Mapping(source = "product.isVariable", target = "isVariable")
    @Mapping(source = "product.hasErc", target = "hasErc")
    @Mapping(source = "offer.id", target = "id")
    @Mapping(source = "offer.hasFee", target = "hasFee")
    @Mapping(source = "offer.productFee", target = "productFee")
    @Mapping(source = "offer.aprc", target = "aprc")
    @Mapping(source = "offer.isAprcHeadline", target= "isAprcHeadline", defaultValue = "false")
    @Mapping(source = "offer.ear", target = "ear")
    @Mapping(source = "offer.svr", target = "svr")
    @Mapping(source = "offer.initialRate", target = "initialRate")
    @Mapping(source = "offer.initialPayment", target = "initialPayment")
    @Mapping(source = "offer.initialMargin", target = "initialMargin")
    @Mapping(source = "offer.reversionPayment", target = "reversionPayment")
    @Mapping(source = "offer.reversionMargin", target = "reversionMargin")
    @Mapping(source = "offer.initialTerm", target = "initialTerm")
    @Mapping(source = "offer.term", target = "term")
    @Mapping(source = "offer.reversionTerm", target = "reversionTerm")
    @Mapping(source = "offer.totalAmountRepaid", target = "totalAmountRepaid")
    @Mapping(source = "offer.maximumLoanAmount", target = "maximumLoanAmount")
    @Mapping(source = "offer.netLoanAmount", target = "netLoanAmount")
    @Mapping(source = "offer.maxErc", target = "maxErc")
    @Mapping(source = "offer.offerBalance", target = "offerBalance")
    @Mapping(source = "offer.requestedLoanAmount", target = "requestedLoanAmount")
    @Mapping(source = "offer.hasProductFeeAddedToLoan", target = "hasProductFeeAddedToLoan")
    @Mapping(source = "offer.ltvCap", target = "ltvCap")
    @Mapping(source = "offer.decision", target = "decision")
    @Mapping(source = "offer.ercPeriodYears", target = "ercPeriodYears")
    @Mapping(source = "offer.ercData", target = "ercData")
    @Mapping(source = "offer.eligibility", target = "eligibility")
    @Mapping(source = "offer.arrangementFeeSelina", target = "arrangementFeeSelina")
    @Mapping(source = "offer.brokerFeesUpfront", target = "brokerFeesUpfront")
    @Mapping(source = "offer.brokerFeesIncluded", target = "brokerFeesIncluded")
    ProductOfferDto mapToProductOfferDto(Product product);
}
