package com.reservationsystem.reservation_backend.security;

import com.reservationsystem.reservation_backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull; // Parametre null kontrolü için
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // UserDetailsService import
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component; // Spring Bean'i olarak işaretlemek için
import org.springframework.web.filter.OncePerRequestFilter; // Her istekte bir kere çalışması için

import java.io.IOException;

/**
 * Gelen isteklerdeki JWT'yi (Authorization header) kontrol eden, doğrulayan
 * ve geçerliyse SecurityContextHolder'a kimlik doğrulama bilgilerini ayarlayan filtre.
 */
@Component // Bu sınıfın bir Spring bileşeni (Bean) olduğunu belirtir
@RequiredArgsConstructor // Lombok: final alanlar için constructor injection
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Her istekte sadece bir kez çalışmasını garantiler

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // UserDetails'i yüklemek için

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, // @NonNull: Bu parametrelerin null olamayacağını belirtir (Lombok)
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain // Sonraki filtreye geçişi sağlar
    ) throws ServletException, IOException {

        // İstek başlıklarından (headers) "Authorization" başlığını al
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail; // Token'dan çıkarılacak kullanıcı adı (e-posta)

        // 1. Authorization başlığı yoksa veya "Bearer " ile başlamıyorsa, filtreyi devam ettir ve çık
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Sonraki filtreye geç
            return; // Bu filtrenin işi bitti
        }

        // 2. "Bearer " kısmını atlayarak JWT'yi al (7. karakterden sonrası)
        jwt = authHeader.substring(7);

        try {
            // 3. Token'dan kullanıcı e-postasını çıkar
            userEmail = jwtService.extractUsername(jwt);

            // 4. E-posta varsa VE mevcut güvenlik bağlamında henüz kimlik doğrulaması yoksa
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 5. Veritabanından UserDetails nesnesini yükle
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // 6. Token'ın geçerli olup olmadığını kontrol et (kullanıcı ve süre bazında)
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // 7. Token geçerliyse, SecurityContextHolder'ı güncellemek için bir Authentication nesnesi oluştur
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, // Principal (kimliği doğrulanmış kullanıcı)
                            null, // Credentials (JWT kullandığımız için parola bilgisine gerek yok)
                            userDetails.getAuthorities() // Yetkiler (roller)
                    );
                    // İstek detaylarını Authentication nesnesine ekle (örn: IP adresi)
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    // SecurityContextHolder'a oluşturulan Authentication nesnesini ayarla
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    // Artık bu istek için kullanıcı kimliği doğrulanmış sayılır.
                }
            }
            // Hata olsa bile (örn: token geçersiz, süre dolmuş) filtre zincirine devam et
            // Eğer token geçersizse SecurityContextHolder'a bir şey set edilmeyecek
            // ve istek korumalı bir endpoint'e gidiyorsa daha sonraki filtrelerde (AuthorizationFilter)
            // kimlik doğrulaması olmadığı için reddedilecektir.
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Token ayrıştırma veya doğrulama sırasında bir hata olursa (örn: MalformedJwtException, ExpiredJwtException)
            // Genellikle burada doğrudan bir yanıt dönmek yerine, hatanın ExceptionTranslationFilter
            // tarafından yakalanıp uygun bir 401 veya 403 yanıtına dönüştürülmesini bekleriz.
            // Bu yüzden genellikle burada özel bir hata yönetimi yapmayıp filtre zincirine devam ederiz.
            // Ancak loglama yapmak faydalı olabilir.
            // logger.warn("JWT token processing error: {}", e.getMessage());
            filterChain.doFilter(request, response); // Zincire devam et
        }
    }
}
