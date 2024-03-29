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

import com.selina.lending.httpclient.middleware.dto.product.response.SelectProductResponse;
import com.selina.lending.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    void selectProduct() {
        //Given
        var applicationId = UUID.randomUUID().toString();
        var externalApplicationId = UUID.randomUUID().toString();
        var productCode = "PR01";
        var message = "Offer selected with success";
        var selectProductResponse = SelectProductResponse.builder().message(message).id(applicationId).build();
        when(productService.selectProductOffer(externalApplicationId, productCode)).thenReturn(selectProductResponse);

        //When
        var appResponse = productController.selectProductOffer(externalApplicationId, productCode);

        //Then
        assertThat(Objects.requireNonNull(appResponse.getBody()).getExternalApplicationId(), equalTo(externalApplicationId));
        assertThat(appResponse.getBody().getProductCode(), equalTo(productCode));
        assertThat(appResponse.getBody().getMessage(), equalTo(message));
        verify(productService, times(1)).selectProductOffer(externalApplicationId, productCode);
    }
}