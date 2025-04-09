package com.reservationsystem.reservation_backend.service;


import com.reservationsystem.reservation_backend.entity.User;
import com.reservationsystem.reservation_backend.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service //  Spring servis bilşeni
@RequiredArgsConstructor // Lombok: Constructor injection için gerekli olan final alanları otomatik olarak oluşturur
public class UserService {
    private final UserRepo userRepo; // Repository sınıfı
    private final PasswordEncoder passwordEncoder;  // Parola şifreleme için kullanılan bileşen

    /**
     * Yeni bir kullanıcı kaydı oluşturur.
     * E-posta adresinin benzersiz olup olmadığını kontrol eder ve parolayı şifreler.
     *
     * @param user Kaydedilecek kullanıcı bilgileri (henüz şifrelenmemiş parola ile).
     * @return Kaydedilen ve veritabanı tarafından ID atanmış kullanıcı nesnesi.
     * @throws RuntimeException Eğer verilen e-posta adresi zaten kullanımdaysa.
     */
    @Transactional // Bu metot bir işlem (transaction) içinde çalışır
    public User registerUser(User user){
        // E-posta adresinin benzersiz olup olmadığını kontrol et(1)
        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());

        if(existingUser.isPresent()){
            throw new RuntimeException("This email already exists");
        }
        // Parolayı şifrele(2)
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Varsayılan rolü ayarla(3)
        if(user.getRole() == null || user.getRole().isBlank()){
            user.setRole("USER"); // Varsayılan rolü USER olarak ayarla
        }

        return userRepo.save(user); // Kullanıcıyı veritabanına kaydet(4)
    }

    /**
     * Verilen e-posta adresine sahip kullanıcıyı bulur.
     *
     * @param email Aranan kullanıcının e-posta adresi.
     * @return Kullanıcıyı içeren bir Optional, bulunamazsa boş Optional.
     */
    @Transactional // Bu metot sadece okuma işlemi yapar
    public Optional<User> findUserByEmail(String email){
        return userRepo.findByEmail(email);
    }

    /**
     * Verilen ID'ye sahip kullanıcıyı bulur.
     *
     * @param id Aranan kullanıcının ID'si.
     * @return Kullanıcıyı içeren bir Optional, bulunamazsa boş Optional.
     */
    @Transactional
    public Optional<User> findUserById(Long id) {
        return userRepo.findById(id);
    }

}
