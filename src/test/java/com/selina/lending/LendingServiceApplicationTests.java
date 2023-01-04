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

package com.selina.lending;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.selina.lending.api.controller.CreditCommitmentsController;
import com.selina.lending.api.controller.DIPController;
import com.selina.lending.api.controller.ProductController;
import com.selina.lending.api.controller.QuickQuoteController;

@SpringBootTest
class LendingServiceApplicationTests {

	@Autowired
	private DIPController dipController;

	@Autowired
	private QuickQuoteController quickQuoteController;

	@Autowired
	private CreditCommitmentsController creditCommitmentsController;

	@Autowired
	private ProductController productController;

	@Test
	void contextLoads() {
		assertNotNull(dipController);
		assertNotNull(quickQuoteController);
		assertNotNull(creditCommitmentsController);
		assertNotNull(productController);
	}
}
