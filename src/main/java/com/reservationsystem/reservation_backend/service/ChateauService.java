package com.reservationsystem.reservation_backend.service;

import com.reservationsystem.reservation_backend.entity.Chateau;
import com.reservationsystem.reservation_backend.exception.ResourceNotFoundException;
import com.reservationsystem.reservation_backend.repo.ChateauRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // loglama işlemleri için
public class ChateauService {

    private final ChateauRepo chateauRepo;

    // Sistemdeki tüm şatoları getirir
    @Transactional
    public List<Chateau> getAllChateaus() {
        log.info("Fetching all chateaus");
        return chateauRepo.findAll();
    }

    @Transactional
    public List<Chateau> getChateausByTheme(String theme){
        if (!StringUtils.hasText(theme)){
            log.warn("Attempted to fetch chateaus with empty theme");
            return Collections.emptyList();
        }
        log.info("Fetching chateaus with theme {}", theme);
        return chateauRepo.findByThemeIgnoreCase(theme);
    }


    //Yeni bir şato oluşturur.
    @Transactional
    public Chateau createChateau(Chateau chateau) {
        log.info("Creating new chateau with name: {}", chateau.getChateauName());
        chateau.setId(null);
        return chateauRepo.save(chateau);
    }

    /**
     * Mevcut bir şatoyu günceller.
     */
    @Transactional
    public Chateau updateChateau(Long id, Chateau chateauDetails) {
        log.info("Updating chateau with id: {}", id);
        // !!! getChateauById yerine doğrudan findById kullanıyoruz !!!
        Chateau existingChateau = chateauRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Chateau not found with id for update: {}", id);
                    return new ResourceNotFoundException("Chateau not found with id: " + id);
                });

        // Alanları güncelle
        existingChateau.setChateauName(chateauDetails.getChateauName());
        existingChateau.setShortDescription(chateauDetails.getShortDescription());
        existingChateau.setLongDescription(chateauDetails.getLongDescription());
        existingChateau.setAddress(chateauDetails.getAddress());
        existingChateau.setLatitude(chateauDetails.getLatitude());
        existingChateau.setLongitude(chateauDetails.getLongitude());
        existingChateau.setChateauWebsite(chateauDetails.getChateauWebsite());
        existingChateau.setOpeningHoursInfo(chateauDetails.getOpeningHoursInfo());
        existingChateau.setTheme(chateauDetails.getTheme()); // theme alanını da güncelle
        existingChateau.setOverallCapacity(chateauDetails.getOverallCapacity());
        existingChateau.setPriceRange(chateauDetails.getPriceRange());
        existingChateau.setBreakfastIncluded(chateauDetails.getBreakfastIncluded());
        existingChateau.setOnSiteActivities(chateauDetails.getOnSiteActivities());
        existingChateau.setOffSiteActivities(chateauDetails.getOffSiteActivities());
        existingChateau.setAdditionalInfo(chateauDetails.getAdditionalInfo());
        existingChateau.setHostName(chateauDetails.getHostName());
        existingChateau.setHostAddress(chateauDetails.getHostAddress());
        existingChateau.setHostPhoneNumber(chateauDetails.getHostPhoneNumber());
        existingChateau.setHostEmail(chateauDetails.getHostEmail());

        // Koleksiyonları güncelle (Önce temizle sonra ekle)
        if (chateauDetails.getSpokenLanguages() != null) {
            existingChateau.getSpokenLanguages().clear();
            existingChateau.getSpokenLanguages().addAll(chateauDetails.getSpokenLanguages());
        }
        if (chateauDetails.getThingsToKnow() != null) {
            existingChateau.getThingsToKnow().clear();
            existingChateau.getThingsToKnow().addAll(chateauDetails.getThingsToKnow());
        }
        if (chateauDetails.getHostSocialMediaLinks() != null) {
            existingChateau.getHostSocialMediaLinks().clear();
            existingChateau.getHostSocialMediaLinks().putAll(chateauDetails.getHostSocialMediaLinks());
        }
        if (chateauDetails.getRoomDescriptions() != null) {
            existingChateau.getRoomDescriptions().clear();
            existingChateau.getRoomDescriptions().addAll(chateauDetails.getRoomDescriptions());
        }
        if (chateauDetails.getImageUrls() != null) {
            existingChateau.getImageUrls().clear();
            existingChateau.getImageUrls().addAll(chateauDetails.getImageUrls());
        }

        return chateauRepo.save(existingChateau);
    }

    //Bir şatoyu siler.

    @Transactional
    public void deleteChateau(Long id) {
        log.warn("Deleting chateau with id: {}", id);
        Chateau existingChateau = chateauRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Chateau not found with id for delete: {}", id);
                    return new ResourceNotFoundException("Chateau not found with id: " + id);
                });
        chateauRepo.delete(existingChateau);
        log.info("Chateau deleted successfully with id: {}", id);
    }

}
