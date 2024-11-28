package com.hhplus.concert.interfaces.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concert.domain.queue.ActiveQueueRepository;
import com.hhplus.concert.domain.queue.Token;
import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import com.hhplus.concert.interfaces.support.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerifyQueueInterceptor implements HandlerInterceptor {

    private static final String QUEUE_TOKEN_HEADER = "Queue-Token";
    private final ActiveQueueRepository activeQueueRepository;
    private final ObjectMapper objectMapper;

    // controller 호출 전 실행
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(QUEUE_TOKEN_HEADER);

        try {
            Token activeQueueToken = activeQueueRepository.getActiveQueueToken(new Token(token, "ACTIVE"));
            if (activeQueueToken == null) {
                throw new CoreException(ErrorType.QUEUE_NOT_FOUND, token);
            }
            return true;
        } catch (CoreException e) {
            writeResponseError(response, e);
            return false;
        }
    }

    // controller 예외 발생 시 실행되지 않음
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // 항상 실행
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    public void writeResponseError(HttpServletResponse response, CoreException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ErrorResponse error = new ErrorResponse(
                e.getErrorType().toString(),
                "FAIL",
                e.getMessage(),
                e.getPayload()
        );
        String data = objectMapper.writeValueAsString(error);
        response.getWriter().write(data);
    }
}
