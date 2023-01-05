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

import com.selina.lending.config.security.permissions.PermissionsVoter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.Arrays;
import java.util.List;

@Import(SecurityProblemSupport.class)
@Configuration(proxyBeanMethods = true)
public class SecurityConfig {

    public static final String LOGIN_URL = "/auth/token";

    private final SecurityProblemSupport problemSupport;
    private final PermissionsVoter permissionsVoter;

    public SecurityConfig(SecurityProblemSupport problemSupport, PermissionsVoter permissionsVoter) {
        this.problemSupport = problemSupport;
        this.permissionsVoter = permissionsVoter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .ignoringAntMatchers("/actuator/**")
                .ignoringAntMatchers(LOGIN_URL)
                .and()
                .cors()
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
                .antMatchers(LOGIN_URL).permitAll()

                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()

                .and()
                .oauth2ResourceServer()
                .jwt();

        http.authorizeRequests().accessDecisionManager(accessDecisionManager());

        return http.build();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters = Arrays.asList(
                new WebExpressionVoter(),
                new RoleVoter(),
                new AuthenticatedVoter(),
                permissionsVoter
        );
        return new UnanimousBased(decisionVoters);
    }
}