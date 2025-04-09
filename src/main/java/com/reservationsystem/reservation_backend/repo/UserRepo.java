package com.reservationsystem.reservation_backend.repo;

import com.reservationsystem.reservation_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Kullanıcı (User) entity'si için veritabanı işlemlerini gerçekleştiren Spring Data JPA repository arayüzü.
 * Temel CRUD (Create, Read, Update, Delete) işlemleri JpaRepository tarafından sağlanır.
 */
@Repository // Bu arayüzün bir Spring bileşeni (Repository) olduğunu belirtir (isteğe bağlı ama önerilir)
public interface UserRepo extends JpaRepository<User, Long> {
    /**
     * Verilen e-posta adresine sahip kullanıcıyı bulur.
     * Spring Data JPA, metot isminden sorguyu otomatik olarak türetir.
     *
     * @param email Aranan kullanıcının e-posta adresi.
     * @return Belirtilen e-posta adresine sahip kullanıcıyı içeren bir Optional nesnesi,
     * eğer bulunamazsa boş bir Optional döner.
     */
    Optional<User> findByEmail(String email);

    // İhtiyaç duyuldukça buraya başka özel sorgu metotları eklenebilir.
    // Örneğin:
    // List<User> findByRole(String role);
    // boolean existsByEmail(String email);

}
