package com.nguyenvanancodeweb.lakesidehotel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Áp dụng cho tất cả các endpoint
                .allowedOrigins("http://localhost:5173",
                        "http://192.168.0.103:8081",   // Metro bundler (có thể không cần)
                        "http://192.168.0.103:19000",  // Thường là IP của Expo dev server
                        "http://192.168.0.103:19006" ) // Chỉ cho phép từ origin này
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức được phép
                .allowedHeaders("*") // Cho phép tất cả các headers
                .allowCredentials(true); // Cho phép gửi cookie cùng yêu cầu
    }
}