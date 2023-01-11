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

package com.selina.lending.api.controller;

import com.selina.lending.internal.service.permissions.annotation.Permission;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.selina.lending.internal.dto.SelectProductResponse;
import com.selina.lending.internal.mapper.SelectProductResponseMapper;
import com.selina.lending.internal.service.ProductService;

import lombok.extern.slf4j.Slf4j;

import static com.selina.lending.internal.service.permissions.annotation.Permission.Resource.DIP;
import static com.selina.lending.internal.service.permissions.annotation.Permission.Scope.Update;

@RestController
@Slf4j
public class ProductController implements ProductOperations{

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    @Permission(resource = DIP, scope = Update)
    public ResponseEntity<SelectProductResponse> selectProductOffer(String externalApplicationId, String productCode) {
        log.info("Select product for [externalApplicationId={}] [productCode={}]", externalApplicationId, productCode);
        var response = SelectProductResponseMapper.mapToSelectProductResponseDto(productService.selectProductOffer(externalApplicationId, productCode),
                externalApplicationId, productCode);
        return ResponseEntity.ok(response);
    }
}
