package com.reservationsystem.reservation_backend.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Kullanıcı girişi için istek gövdesini temsil eden DTO.
 * @param email Kullanıcının e-posta adresi.
 * @param password Kullanıcının parolası.
 */

public record LoginRequest (
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    String email,

    @NotBlank(message = "Password is required")
    String password
){}
