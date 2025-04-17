package com.reservationsystem.reservation_backend.config;

import com.reservationsystem.reservation_backend.security.JwtAuthenticationFilter;
import com.reservationsystem.reservation_backend.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // CSRF disable için modern yol
import org.springframework.security.config.http.SessionCreationPolicy; // Stateless session için
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // JWT filtresi daha sonra eklenecek



/**
 * Spring Security ile ilgili yapılandırma ayarlarını içeren sınıf.
 */
@Configuration // Bu sınıfın Spring yapılandırma sınıfı olduğunu belirtir
@EnableWebSecurity // Spring Security web desteğini aktif eder
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter; // JWT kimlik doğrulama filtresi
    // private final JwtAuthenticationFilter jwtAuthFilter; // Daha sonra eklenecek JWT filtresi
    private final UserDetailsServiceImpl userDetailsService; // Kendi UserDetailsService implementasyonumuz

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
     * Ana güvenlik filtresi zincirini yapılandırır.
     * Hangi endpoint'lerin public, hangilerinin korumalı olacağını,
     * session yönetimini ve CSRF korumasını ayarlar.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF korumasını devre dışı bırak (Stateless API'ler için yaygın)
                .csrf(AbstractHttpConfigurer::disable)
                // Yetkilendirme kuralları
                .authorizeHttpRequests(auth -> auth
                        // Public endpointler:
                        .requestMatchers("/api/auth/**", "/api/user/register").permitAll() // register yolunu kontrol et!
                        // YENİ: Sadece /api/chateaus için GET isteklerine izin ver (filtreleme dahil)
                        .requestMatchers(HttpMethod.GET, "/api/chateaus").permitAll()
                        // Chateau'ları değiştirmek için kimlik doğrulaması iste:
                        .requestMatchers(HttpMethod.POST, "/api/chateaus").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/chateaus/**").authenticated() // ID hala PUT/DELETE için gerekli
                        .requestMatchers(HttpMethod.DELETE, "/api/chateaus/**").authenticated() // ID hala PUT/DELETE için gerekli
                        // Diğer tüm istekler kimlik doğrulaması gerektirir:
                        .anyRequest().authenticated()
                )
                // Session yönetimini STATELESS yap (JWT kullandığımız için session tutmayacağız)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Kullanılacak AuthenticationProvider'ı ayarla
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // JWT filtresini daha sonra ekleyeceğiz

        return http.build();
    }

    /**
     * Kimlik doğrulama yöneticisini (AuthenticationManager) bean olarak expose eder.
     * Login işlemi için Controller'da kullanılır.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Kimlik doğrulama sağlayıcısını (AuthenticationProvider) yapılandırır.
     * UserDetailsService ve PasswordEncoder'ı kullanır.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Kendi UserDetailsService'imizi ayarla
        authProvider.setPasswordEncoder(passwordEncoder()); // Parola şifreleyiciyi ayarla
        return authProvider;
    }




}