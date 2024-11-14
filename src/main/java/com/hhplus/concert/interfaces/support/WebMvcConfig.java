package com.hhplus.concert.interfaces.support;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final VerifyQueueInterceptor verifyQueueInterceptor;

    public WebMvcConfig(VerifyQueueInterceptor verifyQueueInterceptor) {
        this.verifyQueueInterceptor = verifyQueueInterceptor;
    }

    @Bean
    public FilterRegistrationBean<Filter> logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoggingFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(verifyQueueInterceptor)
                .order(1)
                .addPathPatterns(
                        "/api/concerts",
                        "/api/concerts/*",
                        "/api/reservations",
                        "/api/api/payments"
                );
    }
}
