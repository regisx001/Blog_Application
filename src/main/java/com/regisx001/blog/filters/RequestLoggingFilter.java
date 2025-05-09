package com.regisx001.blog.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        filterChain.doFilter(request, response);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String fullUrl = uri + (query != null ? "?" + query : "");

        logger.info("Incoming request: {} {} took {} ms", method, fullUrl, duration);

    }
}
