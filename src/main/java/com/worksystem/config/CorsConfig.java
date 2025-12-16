package com.worksystem.config;// Spring Boot 后端配置示例（其他后端框架逻辑类似）
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许前端域名（3000端口）跨域
        config.addAllowedOrigin("http://localhost:3000");
        // 允许跨域请求携带Cookie/Token
        config.setAllowCredentials(true);
        // 允许所有请求方法（GET/POST等）
        config.addAllowedMethod("*");
        // 允许所有请求头
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有接口生效
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}