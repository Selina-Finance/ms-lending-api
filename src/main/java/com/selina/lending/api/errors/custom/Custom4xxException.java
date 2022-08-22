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

package com.selina.lending.api.errors.custom;

import javax.annotation.Nullable;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class Custom4xxException extends AbstractThrowableProblem {

    public Custom4xxException(@Nullable String details) {
        super(null, "The custom exception title", Status.BAD_REQUEST, details);
    }
}
