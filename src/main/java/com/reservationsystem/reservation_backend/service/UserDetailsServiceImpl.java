package com.reservationsystem.reservation_backend.service;

import com.reservationsystem.reservation_backend.entity.User;
import com.reservationsystem.reservation_backend.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Spring Security için kullanıcı detaylarını yükleyen servis.
 * Kullanıcıyı e-posta adresine göre veritabanından bulur.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    /**
     * Verilen kullanıcı adına (bizim durumumuzda e-posta) göre kullanıcı detaylarını yükler.
     * @param username Kullanıcı adı (e-posta).
     * @return Spring Security'nin anlayacağı UserDetails nesnesi.
     * @throws UsernameNotFoundException Eğer kullanıcı bulunamazsa.
     */
    @Override
    @Transactional(readOnly = true) // Okuma işlemi olduğu için
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Kullanıcıyı e-posta adresine göre bul
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));

        // Kullanıcının rolünü Spring Security'nin anlayacağı formata çevir
        // Şimdilik tek bir rol olduğunu varsayıyoruz. İleride birden fazla rol olabilir.
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase());
        // ÖNEMLİ: Rol isimlerinin başına genellikle "ROLE_" ön eki eklenir.

        // Spring Security'nin UserDetails nesnesini oluştur ve dön
        // Burada Spring'in kendi User sınıfını kullanıyoruz. İsterseniz kendi UserDetails implementasyonunuzu da yapabilirsiniz.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // Kullanıcı adı (e-posta)
                user.getPassword(), // Şifrelenmiş parola (veritabanından geldiği gibi)
                Collections.singletonList(authority) // Kullanıcının rolleri/yetkileri
        );
    }
}
