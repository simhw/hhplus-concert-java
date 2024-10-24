package com.hhplus.concert.interfaces.support;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoggingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        try {
            logRequest(wrappedRequest);
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logResponse(wrappedResponse);
            wrappedResponse.copyBodyToResponse();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String requestBody = getContentAsString(request.getContentAsByteArray(), request.getCharacterEncoding());
        log.info("[REQUEST] Method: {}, URI: {}, Params: {}, Body: {}",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), requestBody);
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        String responseBody = getContentAsString(response.getContentAsByteArray(), response.getCharacterEncoding());
        log.info("[RESPONSE] Status: {}, Body: {}",
                response.getStatus(), responseBody);
    }

    private String getContentAsString(byte[] content, String charset) {
        try {
            return new String(content, charset != null ? charset : StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            log.error("Error reading content", e);
            return "[ERROR]";
        }
    }
}
