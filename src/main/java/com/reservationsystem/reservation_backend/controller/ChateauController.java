package com.reservationsystem.reservation_backend.controller;

import com.reservationsystem.reservation_backend.entity.Chateau;
import com.reservationsystem.reservation_backend.service.ChateauService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/chateaus")
@RequiredArgsConstructor
public class ChateauController {

    private final ChateauService chateauService;

    @GetMapping
    public ResponseEntity<List<Chateau>> getAllOrFilteredChateaus(
            @RequestParam(required = false) String theme
    ) {
        List<Chateau> chateaus;
        if (StringUtils.hasText(theme)) {
            chateaus = chateauService.getChateausByTheme(theme);
        } else {
            chateaus = chateauService.getAllChateaus();
        }
        return ResponseEntity.ok(chateaus);
    }

    @PostMapping
    public ResponseEntity<Chateau> createChateau(@Valid @RequestBody Chateau chateau) {
        Chateau createdChateau = chateauService.createChateau(chateau);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdChateau.getId()).toUri();
        return ResponseEntity.created(location).body(createdChateau);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chateau> updateChateau(@PathVariable Long id, @Valid @RequestBody Chateau chateauDetails) {
        Chateau updatedChateau = chateauService.updateChateau(id, chateauDetails);
        return ResponseEntity.ok(updatedChateau);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChateau(@PathVariable Long id) {
        chateauService.deleteChateau(id);
        return ResponseEntity.noContent().build();
    }
}