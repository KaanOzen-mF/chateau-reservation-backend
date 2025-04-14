package com.reservationsystem.reservation_backend.controller;

import com.reservationsystem.reservation_backend.dto.AuthResponse;
import com.reservationsystem.reservation_backend.dto.LoginRequest;
import com.reservationsystem.reservation_backend.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kimlik doğrulama (Authentication) işlemleri için REST API endpoint'lerini içerir.
 * (Login, Register - Register UserController'da kaldı ama buraya da taşınabilir)
 */
@RestController
@RequestMapping("/api/auth") // Login ve potansiyel diğer auth endpoint'leri için baz yol
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    // private final UserService userService; // UserDetails almak için alternatif olarak UserDetailsService kullanılabilir

    /**
     * Kullanıcı girişi için POST endpoint'i.
     * Başarılı girişte JWT döner.
     * @param loginRequest E-posta ve parola içeren istek DTO'su.
     * @return HTTP 200 OK ve JWT içeren AuthResponse.
     * Hatalı girişte (BadCredentialsException) 401 Unauthorized döner (GlobalExceptionHandler tarafından).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // 1. Spring Security AuthenticationManager ile kimlik doğrulama yapmayı dene
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(), // Principal (kullanıcı adı/e-posta)
                        loginRequest.password() // Credentials (parola)
                )
        );

        // 2. Kimlik doğrulama başarılıysa, Principal'dan UserDetails'i al
        // Principal genellikle UserDetails nesnesidir (bizim UserDetailsServiceImpl'den dönen)
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. JWT oluştur
        String jwtToken = jwtService.generateToken(userDetails);

        // 4. JWT'yi AuthResponse içinde dön
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }
}

