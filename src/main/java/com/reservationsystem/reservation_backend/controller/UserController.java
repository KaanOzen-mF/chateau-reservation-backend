package com.reservationsystem.reservation_backend.controller;


import com.reservationsystem.reservation_backend.entity.User;
import com.reservationsystem.reservation_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Rest Controller ve @ResponseBody'yi birleştirir
@RequestMapping("/api/user") //Bu controller için tüm endpoint'lerin temel URL'si
@RequiredArgsConstructor // Lombok: Constructor injection için gerekli olan final alanları otomatik olarak oluşturur
public class UserController {
    private final UserService userService;

    /**
     * Yeni bir kullanıcı kaydı oluşturmak için POST endpoint'i.
     * İstek gövdesinde gelen User bilgilerini alır, doğrular ve kaydeder.
     * Başarılı olursa HTTP 201 (Created) durumu ve kaydedilen kullanıcı bilgisini döner.
     *
     * @param user İstek gövdesinden (JSON) alınan ve doğrulanacak User nesnesi.
     * @return HTTP 201 ve kaydedilmiş User nesnesini içeren ResponseEntity.
     * Eğer e-posta zaten varsa veya doğrulama hatası olursa uygun hata döner (varsayılan Spring hatası).
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user){

        User savedUser = userService.registerUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


}
