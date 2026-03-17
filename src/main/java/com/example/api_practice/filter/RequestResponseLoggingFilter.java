package com.example.api_practice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter{

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("**** NEW FILTER ACTIVE ****");

        ContentCachingRequestWrapper requestWrapper =
                new ContentCachingRequestWrapper(request, 1024 * 1024);

        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - start;

            String requestBody = new String(
                    requestWrapper.getContentAsByteArray(),
                    StandardCharsets.UTF_8
            );

            String responseBody = new String(
                    responseWrapper.getContentAsByteArray(),
                    StandardCharsets.UTF_8
            );

            log.info(
                    "API REQUEST method={} uri={} body={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    requestBody
            );

            log.info(
                    "API RESPONSE method={} uri={} status={} duration={}ms body={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration,
                    responseBody
            );

            responseWrapper.copyBodyToResponse();
        }
    }
}
