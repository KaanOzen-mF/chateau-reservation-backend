package com.reservationsystem.reservation_backend.dto;

/**
 * Başarılı kimlik doğrulama sonrası istemciye dönülecek yanıtı temsil eden DTO.
 * @param accessToken Oluşturulan JWT (Bearer token).
 */
public record AuthResponse( String accessToken) {
}
