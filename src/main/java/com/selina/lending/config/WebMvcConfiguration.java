///*
// * Copyright 2022 Selina Finance
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// */
//
//package com.selina.lending.config;
//
//import com.selina.lending.api.interceptor.BrokerRequestInterceptor;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@ConditionalOnProperty(value = "kafka.enable", havingValue = "true", matchIfMissing = true)
//public class WebMvcConfiguration implements WebMvcConfigurer {
//
//    private final BrokerRequestInterceptor brokerRequestInterceptor;
//
//    public WebMvcConfiguration(BrokerRequestInterceptor brokerRequestInterceptor) {
//        this.brokerRequestInterceptor = brokerRequestInterceptor;
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(brokerRequestInterceptor)
//                .excludePathPatterns("/auth/**")
//                .order(Ordered.HIGHEST_PRECEDENCE);
//        WebMvcConfigurer.super.addInterceptors(registry);
//    }
//}
