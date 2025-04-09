package com.reservationsystem.reservation_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Sistemdeki kullanıcıları temsil eden JPA entity sınıfı.
 * Veritabanındaki 'users' tablosuyla eşleşir.
 */
@Data // Lombok: Getter, Setter, toString, equals, hashCode, RequiredArgsConstructor
@NoArgsConstructor // Lombok: Parametresiz constructor (JPA için gerekli)
@AllArgsConstructor // Lombok: Tüm alanları içeren constructor
@Builder // Lombok: Builder tasarım deseni implementasyonu
@Entity // Bu sınıfın bir veritabanı varlığı (entity) olduğunu belirtir
@Table(name = "users") // Veritabanında eşleşeceği tablonun adını belirtir

public class User {
    /**
     * Kullanıcının benzersiz kimliği (Primary Key).
     * Veritabanı tarafından otomatik olarak artırılır (IDENTITY stratejisi).
     */
    @Id  // Bu alanın primary key olduğunu belirtir
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID'nin otomatik artan olmasını sağlar (PostgreSQL için uygun)
    private Long id;

    /**
     * Kullanıcının adı. Boş olamaz.
     */
    @NotBlank(message = "First name is required") // Alanın null veya sadece boşluklardan oluşmamasını sağlar
    @Column(nullable = false) // Veritabanı sütununun null olamayacağını belirtir
    private String firstName;

    /**
     * Kullanıcının soyadı. Boş olamaz.
     */
    @NotBlank(message = "Last name is required") // Alanın null veya sadece boşluklardan oluşmamasını sağlar
    @Column(nullable = false)
    private String lastName;

    /**
     * Kullanıcının e-posta adresi. Benzersiz olmalı ve geçerli bir e-posta formatında olmalıdır.
     */
    @NotBlank(message = "Email is required") // Alanın null veya sadece boşluklardan oluşmamasını sağlar
    @Email(message = "Please write a valid format") // Alanın e-posta formatında olmasını sağlar
    @Column(nullable = false, unique = true) // Veritabanında benzersiz (unique) ve null olamaz
    private String email;

    /**
     * Kullanıcının şifrelenmiş parolası. Boş olamaz.
     * Güvenlik nedeniyle veritabanında her zaman hash'lenmiş olarak saklanmalıdır.
     */
    @NotBlank(message = "Password is required") // Alanın null veya sadece boşluklardan oluşmamasını sağlar
    @Size(min = 6, message = "Password is minumum 6 characters") // Örnek bir boyut kısıtlaması
    @Column(nullable = false)
    private String password;

    /**
     * Kullanıcının sistemdeki rolü (Örn: "USER", "ADMIN").
     * Yetkilendirme için kullanılır. Şimdilik String, ileride Enum veya ayrı bir Entity olabilir.
     */
    @NotBlank(message = "Role is required") // Alanın null veya sadece boşluklardan oluşmamasını sağlar
    @Column(nullable = false)
    private String role = "USER"; // Varsayılan rol olarak "USER" atanabilir

    /**
     * Kullanıcı kaydının oluşturulma zamanı. Otomatik olarak ayarlanması tercih edilir.
     * Bu alan güncellenemez.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Kullanıcı kaydının son güncellenme zamanı. Otomatik olarak ayarlanması tercih edilir.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // JPA Lifecycle Callback'leri ile createdAt ve updatedAt alanlarını otomatik ayarlama
    @PrePersist // Kayıt veritabanına ilk kez eklenmeden hemen önce çalışır
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now(); // İlk oluşturmada updatedAt de createdAt ile aynı olsun
    }

    @PreUpdate // Kayıt veritabanında güncellenmeden hemen önce çalışır
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
