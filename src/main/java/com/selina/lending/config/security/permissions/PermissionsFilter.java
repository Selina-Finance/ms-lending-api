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
//package com.selina.lending.config.security.permissions;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.GenericFilterBean;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import java.io.IOException;
//
//public class PermissionsFilter extends GenericFilterBean {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//        // we get the authenticated user from the context
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Fetch from the request:
//        // 1. path
//        // 2. http method
//        // 3. user jwt token
//
//        // Form permissions request and call permission service
//
//
//
//        chain.doFilter(request, response);
//    }
//}
