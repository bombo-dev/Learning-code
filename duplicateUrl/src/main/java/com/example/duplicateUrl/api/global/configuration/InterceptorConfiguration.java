package com.example.duplicateUrl.api.global.configuration;

import com.example.duplicateUrl.api.global.interceptor.DuplicateRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final DuplicateRequestInterceptor duplicateRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(duplicateRequestInterceptor)
                .addPathPatterns("/**");
    }
}
