package com.reservationsystem.reservation_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security ile ilgili yapılandırma ayarlarını içeren sınıf.
 */
@Configuration // Bu sınıfın Spring yapılandırma sınıfı olduğunu belirtir
@EnableWebSecurity // Spring Security web desteğini aktif eder
public class SecurityConfig {

    /**
     * Parola şifreleme için kullanılacak PasswordEncoder bean'ini oluşturur.
     * BCrypt, güçlü bir hash algoritmasıdır ve yaygın olarak tavsiye edilir.
     * @return PasswordEncoder arayüzünü implemente eden bir BCryptPasswordEncoder nesnesi.
     */
    @Bean // Bu metot tarafından dönen nesnenin bir Spring bean'i olarak yönetilmesini sağlar
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * HTTP Güvenlik kurallarını yapılandıran SecurityFilterChain bean'ini tanımlar.
     * Hangi endpoint'lerin herkese açık olacağını, hangilerinin kimlik doğrulaması gerektireceğini belirler.
     *
     * @param http HttpSecurity nesnesi, güvenlik kurallarını yapılandırmak için kullanılır.
     * @return Yapılandırılmış SecurityFilterChain nesnesi.
     * @throws Exception Yapılandırma sırasında bir hata oluşursa.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF (Cross-Site Request Forgery) korumasını devre dışı bırakıyoruz.
                // REST API'ler genellikle stateless olduğu ve token tabanlı kimlik doğrulama kullandığı için
                // CSRF koruması genellikle gereksizdir veya farklı yöntemlerle sağlanır.
                // Tarayıcı tabanlı session yönetimi kullanılıyorsa etkinleştirilmelidir.
                .csrf(AbstractHttpConfigurer::disable) // Yeni yöntem (lambda DSL)

                // HTTP istekleri için yetkilendirme kurallarını yapılandırıyoruz.
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // /api/users/register endpoint'ine gelen tüm isteklere (authenticated veya unauthenticated) izin ver.
                                .requestMatchers("/api/users/register").permitAll()
                                // TODO: Login endpoint'i eklendiğinde buraya "/api/users/login" de eklenmeli.
                                // TODO: Swagger/OpenAPI endpoint'leri eklendiğinde onlar da buraya eklenebilir (örn: /v3/api-docs/**, /swagger-ui/**)

                                // Yukarıda belirtilenler dışındaki TÜM diğer isteklilerin kimlik doğrulaması (authenticated) gerektirmesini sağla.
                                .anyRequest().authenticated()
                );
        // Varsayılan olarak gelen form login ve http basic auth gibi mekanizmalar
        // JWT implemente edildiğinde genellikle devre dışı bırakılır veya özelleştirilir.
        // Şimdilik varsayılan ayarlarla bırakabiliriz veya .httpBasic(withDefaults()) gibi açıkça belirtebiliriz.

        return http.build();
    }
    // Not: Spring Security'nin diğer yapılandırmaları (HTTP güvenliği,
    // yetkilendirme kuralları vb.) daha sonra bu sınıfa eklenecektir.
}