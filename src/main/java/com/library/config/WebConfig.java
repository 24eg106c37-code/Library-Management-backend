package com.library.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @NonNull private final AuthInterceptor authInterceptor;
    private final String frontendOrigin;
    public WebConfig(@NonNull AuthInterceptor authInterceptor, @Value("${library.frontend-origin}") String frontendOrigin) {
        this.authInterceptor = authInterceptor; this.frontendOrigin = frontendOrigin;
    }
    @Override public void addInterceptors(@NonNull InterceptorRegistry registry) { registry.addInterceptor(authInterceptor).addPathPatterns("/api/**"); }
    @Override public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins(frontendOrigin, "http://127.0.0.1:5500")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*");
    }
}