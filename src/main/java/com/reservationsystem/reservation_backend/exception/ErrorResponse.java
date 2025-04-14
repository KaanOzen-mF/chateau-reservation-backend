package com.reservationsystem.reservation_backend.exception;


import java.time.LocalDateTime;
import java.util.Map;

/**
 * API hatalarında istemciye dönülecek standart hata yanıtı yapısı.
 *
 * @param timestamp Hatanın oluştuğu zaman damgası.
 * @param status HTTP durum kodu.
 * @param error HTTP durum açıklaması (örn: "Bad Request", "Not Found").
 * @param message Hatayı açıklayan genel mesaj.
 * @param path Hatanın oluştuğu istek yolu.
 * @param validationErrors (İsteğe Bağlı) Doğrulama hatalarının detayları (alan -> hata mesajı).
 */

public record ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path, Map<String, String> validationErrors)  {
    // Sadece genel mesajlar için validationErrors olmadan kullanılabilecek ek constructor'lar
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this(timestamp, status, error, message, path, null);
    }
}
