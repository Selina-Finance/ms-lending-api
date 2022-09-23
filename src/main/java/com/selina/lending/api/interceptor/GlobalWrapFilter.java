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
//package com.selina.lending.api.interceptor;
//
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.ContentCachingResponseWrapper;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ReadListener;
//import javax.servlet.ServletException;
//import javax.servlet.ServletInputStream;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.Charset;
//import java.util.stream.Collectors;
//
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class GlobalWrapFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        MultiReadRequest wrapper = new MultiReadRequest((HttpServletRequest) request);
//
//
//        chain.doFilter(wrapper, response);
//    }
////
////    @Override
////    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
////
////        ContentCachingResponseWrapper responseCacheWrapperObject = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);
////        filterChain.doFilter(servletRequest, responseCacheWrapperObject);
////
//////        servletRequestbyte[] responseArray = responseCacheWrapperObject.getContentAsByteArray();
//////        String responseStr = new String(responseArray, responseCacheWrapperObject.getCharacterEncoding());
////        //....use responsestr to make the signature
////
////        responseCacheWrapperObject.copyBodyToResponse();
////
////        chain.doFilter(wrapper, response);
////
////    }
////
//    @Override
//    public void destroy() {
//    }
//
//    class MultiReadRequest extends HttpServletRequestWrapper {
//
//        private String requestBody;
//
//        public MultiReadRequest(HttpServletRequest request) {
//            super(request);
//            try {
//                requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public ServletInputStream getInputStream() throws IOException {
//            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody.getBytes());
//            return new ServletInputStream() {
//                @Override
//                public boolean isFinished() {
//                    return byteArrayInputStream.available() == 0;
//                }
//
//                @Override
//                public boolean isReady() {
//                    return true;
//                }
//
//                @Override
//                public void setReadListener(ReadListener readListener) {
//
//                }
//
//                @Override
//                public int read() throws IOException {
//                    return byteArrayInputStream.read();
//                }
//            };
//        }
//
//        @Override
//        public BufferedReader getReader() throws IOException {
//            return new BufferedReader(new InputStreamReader(this.getInputStream(), Charset.forName("UTF-8")));
//        }
//    }
//}
