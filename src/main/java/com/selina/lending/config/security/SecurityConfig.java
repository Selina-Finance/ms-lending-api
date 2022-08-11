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

package com.selina.lending.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Import(SecurityProblemSupport.class)
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    private final SecurityProblemSupport problemSupport;

    public SecurityConfig(SecurityProblemSupport problemSupport) {
        this.problemSupport = problemSupport;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/**").permitAll().anyRequest().authenticated().and().csrf().disable();
        http.cors()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)

                .and()
                .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()

                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()

                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
}