package com.nguyenvanancodeweb.lakesidehotel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Áp dụng cho tất cả các endpoint
//                .allowedOrigins("*") // Chỉ cho phép từ origin này
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức được phép
//                .allowedHeaders("*"); // Cho phép tất cả các headers
                // Cho phép gửi cookie cùng yêu cầu
                .allowedOriginPatterns("*") // ✅ dùng wildcard khi muốn cho tất cả
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}