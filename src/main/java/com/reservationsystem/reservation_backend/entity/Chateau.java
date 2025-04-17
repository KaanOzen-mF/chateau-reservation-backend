package com.reservationsystem.reservation_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chateau")
public class Chateau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Şato Detayları ---
    @NotBlank(message = "Chateau name is mandatory")
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String chateauName;

    @Size(max = 500)
    @Column(length = 500)
    private String shortDescription;

    @NotBlank(message = "Chateau description is mandatory")
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String longDescription;

    @NotBlank(message = "Chateau address is mandatory")
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String address;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(length = 2048)
    private String chateauWebsite;

    @Column(length = 255)
    private String openingHoursInfo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "chateau_languages", joinColumns = @JoinColumn(name = "chateau_id"))
    @Column(name = "language", nullable = false)
    private Set<String> spokenLanguages = new HashSet<>();

    @Lob
    @Column(columnDefinition = "TEXT")
    private String onSiteActivities;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String offSiteActivities;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "chateau_to_know", joinColumns = @JoinColumn(name = "chateau_id"))
    @Column(name = "info", nullable = false)
    private Set<String> thingsToKnow = new HashSet<>();

    @Lob
    @Column(columnDefinition = "TEXT")
    private String additionalInfo; // İsim güncellenmiş

    @Column(length = 100)
    private String priceRange;

    @Column
    private String breakfastIncluded;

    @Column
    private Integer overallCapacity;

    @Column(length = 100)
    private String theme;

    // --- Host Detayları ---
    @Column(length = 255)
    private String hostName;

    @Column(length = 500)
    private String hostAddress;

    @Column(length = 50)
    private String hostPhoneNumber;

    @Email(message = "Invalid email format")
    @Column(length = 255)
    private String hostEmail;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "chateau_host_social_media", joinColumns = @JoinColumn(name = "chateau_id"))
    @MapKeyColumn(name = "platform")
    @Column(name = "url", length = 2048)
    private Map<String, String> hostSocialMediaLinks = new HashMap<>();

    // --- Oda Bilgileri (String Listesi) ---
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "chateau_room_descriptions", joinColumns = @JoinColumn(name = "chateau_id"))
    @OrderColumn(name = "room_order")
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private List<String> roomDescriptions = new ArrayList<>();

    // --- Resim Bilgileri (URL Listesi) ---
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "chateau_image_urls", joinColumns = @JoinColumn(name = "chateau_id"))
    @Column(name = "image_url", length = 2048)
    @OrderColumn(name = "image_order")
    private List<String> imageUrls = new ArrayList<>();


    // --- Helper Metotlar ---
    public void addImageUrl(String url) {
        this.imageUrls.add(url);
    }
    public void removeImageUrl(String url) {
        this.imageUrls.remove(url);
    }
    public void addRoomDescription(String description) {
        this.roomDescriptions.add(description);
    }
    public void removeRoomDescription(String description) {
        this.roomDescriptions.remove(description);
    }
    public void clearRoomDescriptions() {
        this.roomDescriptions.clear();
    }
}