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
@Slf4j
public class ChateauService {

    private final ChateauRepo chateauRepo;

    @Transactional(readOnly = true)
    public List<Chateau> getAllChateaus() {
        log.info("Fetching all chateaus");
        return chateauRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Chateau> getChateausByTheme(String theme) {
        if (!StringUtils.hasText(theme)) {
            log.warn("Attempted to search chateaus with empty theme.");
            return Collections.emptyList();
        }
        log.info("Fetching chateaus with theme: {}", theme);
        return chateauRepo.findByThemeIgnoreCase(theme);
    }

    @Transactional
    public Chateau createChateau(Chateau chateau) {
        log.info("Creating new chateau with name: {}", chateau.getChateauName());
        chateau.setId(null);
        return chateauRepo.save(chateau);
    }

    @Transactional
    public Chateau updateChateau(Long id, Chateau chateauDetails) {
        log.info("Updating chateau with id: {}", id);
        Chateau existingChateau = chateauRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chateau not found with id: " + id));

        // Alanları güncelle (String alanlar)
        existingChateau.setChateauName(chateauDetails.getChateauName());
        existingChateau.setShortDescription(chateauDetails.getShortDescription());
        existingChateau.setAddress(chateauDetails.getAddress()); // Artık Liste!
        existingChateau.setLatitude(chateauDetails.getLatitude());
        existingChateau.setLongitude(chateauDetails.getLongitude());
        existingChateau.setChateauWebsite(chateauDetails.getChateauWebsite());
        existingChateau.setOpeningHoursInfo(chateauDetails.getOpeningHoursInfo());
        existingChateau.setBreakfastIncluded(chateauDetails.getBreakfastIncluded());
        existingChateau.setOverallCapacity(chateauDetails.getOverallCapacity());
        existingChateau.setTheme(chateauDetails.getTheme());
        existingChateau.setHostName(chateauDetails.getHostName());
        existingChateau.setHostAddress(chateauDetails.getHostAddress());
        existingChateau.setHostPhoneNumber(chateauDetails.getHostPhoneNumber());
        existingChateau.setHostEmail(chateauDetails.getHostEmail());

        // Koleksiyonları (List<String>, Set<String>, Map<String, String>) güncelle
        // (clear/addAll veya clear/putAll yaklaşımı)
        updateCollection(existingChateau.getLongDescription(), chateauDetails.getLongDescription());
        updateCollection(existingChateau.getAddress(), chateauDetails.getAddress()); // Adres de liste oldu
        updateCollection(existingChateau.getSpokenLanguages(), chateauDetails.getSpokenLanguages());
        updateCollection(existingChateau.getOnSiteActivities(), chateauDetails.getOnSiteActivities());
        updateCollection(existingChateau.getOffSiteActivities(), chateauDetails.getOffSiteActivities());
        updateCollection(existingChateau.getThingsToKnow(), chateauDetails.getThingsToKnow());
        updateCollection(existingChateau.getAdditionalInfo(), chateauDetails.getAdditionalInfo());
        updateCollection(existingChateau.getPriceRange(), chateauDetails.getPriceRange());
        updateCollection(existingChateau.getRoomDescriptions(), chateauDetails.getRoomDescriptions());
        updateCollection(existingChateau.getImageUrls(), chateauDetails.getImageUrls());

        if (chateauDetails.getHostSocialMediaLinks() != null) {
            existingChateau.getHostSocialMediaLinks().clear();
            existingChateau.getHostSocialMediaLinks().putAll(chateauDetails.getHostSocialMediaLinks());
        }


        return chateauRepo.save(existingChateau);
    }

    // Koleksiyonları güncellemek için yardımcı metot (List ve Set için çalışır)
    private <T> void updateCollection(Collection<T> existingCollection, Collection<T> newCollection) {
        if (newCollection != null) {
            existingCollection.clear();
            existingCollection.addAll(newCollection);
        }
    }


    @Transactional
    public void deleteChateau(Long id) {
        log.warn("Deleting chateau with id: {}", id);
        Chateau existingChateau = chateauRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chateau not found with id: " + id));
        chateauRepo.delete(existingChateau);
        log.info("Chateau deleted successfully with id: {}", id);
    }
}