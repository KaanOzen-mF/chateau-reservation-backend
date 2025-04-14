package com.reservationsystem.reservation_backend.exception;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Uygulama genelindeki istisnaları (exceptions) yakalayıp,
 * istemciye standart bir formatta hata yanıtları dönen merkezi handler sınıfı.
 */

@ControllerAdvice
@Slf4j // Lombok: Loglama için gerekli olan bileşen
public class GlobalExceptionHandler {
    /**
     * Jakarta Bean Validation (@Valid) hatalarını yakalar.
     * (MethodArgumentNotValidException)
     * @param ex Yakalanan exception.
     * @param request Hatanın oluştuğu HTTP isteği.
     * @return HTTP 400 Bad Request ve detaylı hata mesajlarını içeren ResponseEntity.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Doğrulama Hatası", // Genel mesaj
                request.getRequestURI(),
                errors // Alan bazlı hatalar
        );
        log.warn("Doğrulama Hatası: Path={}, Hatalar={}", request.getRequestURI(), errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Kaynağın zaten var olduğunu belirten özel exception'ı yakalar.
     * (ResourceAlreadyExistsException)
     * @param ex Yakalanan exception.
     * @param request Hatanın oluştuğu HTTP isteği.
     * @return HTTP 409 Conflict ve hata mesajını içeren ResponseEntity.
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(), // Exception'ın kendi mesajını kullanıyoruz
                request.getRequestURI()
        );
        log.warn("Kaynak Zaten Mevcut Hatası: Path={}, Mesaj={}", request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Hatalı kullanıcı adı veya parola girildiğinde fırlatılan exception'ı yakalar.
     * (BadCredentialsException)
     * @param ex Yakalanan exception.
     * @param request Hatanın oluştuğu HTTP isteği.
     * @return HTTP 401 Unauthorized ve hata mesajını içeren ResponseEntity.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Geçersiz e-posta veya şifre.", // Kullanıcıya daha genel bir mesaj
                request.getRequestURI()
        );
        log.warn("Kimlik Doğrulama Hatası: Path={}, Mesaj={}", request.getRequestURI(), ex.getMessage()); // Orijinal mesajı logla
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Yukarıdaki özel handler'lar tarafından yakalanmayan tüm diğer genel exception'ları yakalar.
     * Bu bir "catch-all" handler'dır.
     * @param ex Yakalanan genel exception.
     * @param request Hatanın oluştuğu HTTP isteği.
     * @return HTTP 500 Internal Server Error ve genel bir hata mesajı içeren ResponseEntity.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Beklenmedik bir sunucu hatası oluştu.", // İstemciye detay sızdırmayan genel mesaj
                request.getRequestURI()
        );
        // Genel hataları daha detaylı loglamak önemlidir!
        log.error("Beklenmedik Hata: Path={}, Hata Mesajı={}", request.getRequestURI(), ex.getMessage(), ex); // Stack trace'i de logla
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
