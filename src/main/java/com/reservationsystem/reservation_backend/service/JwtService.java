package com.reservationsystem.reservation_backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value; // @Value için
import org.springframework.security.core.userdetails.UserDetails; // UserDetails için
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey; // SecretKey için
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


//JWT (JSON Web Token) oluşturma, doğrulama ve ayrıştırma işlemlerini yöneten servis.

@Service
public class JwtService {

    // JWT'nin geçerlilik süresi (milisaniye cinsinden )
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    // JWT imzalamak için kullanılacak gizli anahtar (Base64 encoded)
    // Bu anahtar çok gizli tutulmalı ve application.properties/environment variable gibi yerlerde saklanmalıdır.
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    /**
     * Verilen UserDetails nesnesi için bir JWT oluşturur.
     * @param userDetails Kimlik doğrulaması yapılmış kullanıcı detayları.
     * @return Oluşturulan JWT String'i.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Ekstra talepler (claims) ile birlikte bir JWT oluşturur.
     * @param extraClaims Token'a eklenecek ekstra bilgiler (örn: roller).
     * @param userDetails Kimlik doğrulaması yapılmış kullanıcı detayları.
     * @return Oluşturulan JWT String'i.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    // Token oluşturma işleminin yapıldığı özel metot
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .setClaims(extraClaims) // Ekstra talepleri ayarla
                .setSubject(userDetails.getUsername()) // Token'ın konusunu (genellikle kullanıcı adı/e-posta) ayarla
                .setIssuedAt(new Date(System.currentTimeMillis())) // Token'ın oluşturulma zamanını ayarla
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Token'ın bitiş zamanını ayarla
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Gizli anahtar ve algoritma ile imzala
                .compact(); // Token'ı String olarak oluştur
    }

    /**
     * Verilen token'ın geçerli olup olmadığını kontrol eder.
     * Kullanıcı adının token'dan çıkarılıp UserDetails ile eşleşip eşleşmediğini ve token'ın süresinin dolup dolmadığını kontrol eder.
     * @param token Kontrol edilecek JWT.
     * @param userDetails Karşılaştırılacak kullanıcı detayları.
     * @return Token geçerliyse true, değilse false.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Token'ın süresinin dolup dolmadığını kontrol eder
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Token'dan son kullanma tarihini çıkarır
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Verilen token'dan kullanıcı adını (subject) çıkarır.
     * @param token JWT.
     * @return Kullanıcı adı (e-posta).
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Token'dan belirli bir talebi (claim) çıkarır.
     * @param token JWT.
     * @param claimsResolver Talebi çıkaran fonksiyon.
     * @param <T> Talebin tipi.
     * @return Çıkarılan talep.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Token'daki tüm talepleri (claims) çıkarır
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey()) // Doğrulama için gizli anahtarı ayarla
                .build()
                .parseClaimsJws(token) // Token'ı ayrıştır ve doğrula
                .getBody(); // Talepleri al
    }

    // Base64 formatındaki gizli anahtarı çözüp SecretKey nesnesi oluşturur
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
