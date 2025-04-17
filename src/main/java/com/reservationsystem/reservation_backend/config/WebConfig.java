package com.reservationsystem.reservation_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull; // @NonNull importu
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/api/**") // /api altındaki tüm yollara CORS uygula
                        .allowedOrigins("http://localhost:3000") // Next.js uygulamanızın çalıştığı adres
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // İzin verilen HTTP metotları
                        .allowedHeaders("*") // Tüm başlıklara izin ver
                        .allowCredentials(true); // Kimlik bilgileri (cookie vb.) gerekiyorsa
            }
        };
    }
}
