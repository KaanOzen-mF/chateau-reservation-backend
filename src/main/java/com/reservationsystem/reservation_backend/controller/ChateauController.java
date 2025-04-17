package com.reservationsystem.reservation_backend.controller;

import com.reservationsystem.reservation_backend.entity.Chateau;
import com.reservationsystem.reservation_backend.service.ChateauService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/chateaus")
@RequiredArgsConstructor
public class ChateauController {
    private final ChateauService chateauService;

    // /api/chateaus?theme=Romantic
    @GetMapping
    public ResponseEntity<List<Chateau>> getAllOrFilteredChateaus(
            @RequestParam(required = false) String theme // theme query parametresini al
    ) {
        List<Chateau> chateaus;
        // StringUtils.hasText ile theme null değil VE boş değil kontrolü
        if (StringUtils.hasText(theme)) {
            // Eğer theme parametresi varsa, servisteki filtreleme metodunu çağır
            chateaus = chateauService.getChateausByTheme(theme);
        } else {
            // Eğer theme parametresi yoksa, tüm şatoları getiren metodu çağır
            chateaus = chateauService.getAllChateaus();
        }
        return ResponseEntity.ok(chateaus);
    }


    //Yeni bir şato oluşturur.
    @PostMapping
    public ResponseEntity<Chateau> createChateau(@Valid @RequestBody Chateau chateau) {
        Chateau createdChateau = chateauService.createChateau(chateau);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdChateau.getId()).toUri();
        return ResponseEntity.created(location).body(createdChateau);
    }


    //Belirtilen ID'ye sahip şatoyu günceller.
    @PutMapping("/{id}")
    public ResponseEntity<Chateau> updateChateau(@PathVariable Long id, @Valid @RequestBody Chateau chateauDetails) {
        Chateau updatedChateau = chateauService.updateChateau(id, chateauDetails);
        return ResponseEntity.ok(updatedChateau);
    }

    //Belirtilen ID'ye sahip şatoyu siler.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChateau(@PathVariable Long id) {
        chateauService.deleteChateau(id);
        return ResponseEntity.noContent().build();
    }

}
