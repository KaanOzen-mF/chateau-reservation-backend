package com.reservationsystem.reservation_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; // Zaman damgası olmayacaksa bu import gereksiz
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

    // !!! List<String> olarak güncellendi !!!
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "chateau_long_descriptions", joinColumns = @JoinColumn(name = "chateau_id"))
    @OrderColumn(name = "paragraph_order")
    @Column(name = "paragraph", nullable = false, columnDefinition = "TEXT")
    private List<String> longDescription = new ArrayList<>();

    // !!! List<String> olarak güncellendi !!!
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "chateau_address_lines", joinColumns = @JoinColumn(name = "chateau_id"))
    @OrderColumn(name = "line_order")
    @Column(name = "address_line", nullable = false, length = 500)
    private List<String> address = new ArrayList<>();

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(length = 2048)
    private String chateauWebsite;

    @Column(length = 255)
    private String openingHoursInfo;

    // !!! List<String> olarak güncellendi !!!
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "chateau_languages", joinColumns = @JoinColumn(name = "chateau_id"))
    @OrderColumn(name = "language_order")
    @Column(name = "language", nullable = false)
    private List<String> spokenLanguages = new ArrayList<>();

    // !!! List<String> olarak güncellendi !!!
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "chateau_onsite_activities", joinColumns = @JoinColumn(name = "chateau_id"))
    @OrderColumn(name = "activity_order")
    @Column(name = "activity", nullable = false, columnDefinition = "TEXT")
    private List<String> onSiteActivities = new ArrayList<>();

    // !!! List<String> olarak güncellendi !!!
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "chateau_offsite_activities", joinColumns = @JoinColumn(name = "chateau_id"))
    @OrderColumn(name = "activity_order")
    @Column(name = "activity", nullable = false, columnDefinition = "TEXT")
    private List<String> offSiteActivities = new ArrayList<>();

    // !!! List<String> olarak güncellendi !!!
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "chateau_to_know", joinColumns = @JoinColumn(name = "chateau_id"))
    @OrderColumn(name = "info_order")
    @Column(name = "info", nullable = false)
    private List<String> thingsToKnow = new ArrayList<>();

    // !!! List<String> olarak güncellendi !!!
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "chateau_additional_info", joinColumns = @JoinColumn(name = "chateau_id"))
    @OrderColumn(name = "info_order")
    @Column(name = "info_line", nullable = false, columnDefinition = "TEXT")
    private List<String> additionalInfo = new ArrayList<>();

    // !!! List<String> olarak güncellendi !!!
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "chateau_price_info", joinColumns = @JoinColumn(name = "chateau_id"))
    @OrderColumn(name = "price_order")
    @Column(name = "price_line", nullable = false, length = 100)
    private List<String> priceRange = new ArrayList<>();

    @Column
    private String breakfastIncluded; // String olarak bırakıldı

    @Column
    private Integer overallCapacity;

    @Column(length = 100)
    private String theme;

    // --- Host Detayları (Gömülü) ---
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

    // Zaman damgaları kaldırıldı
    // Helper metotlar aynı kalabilir (imageUrls ve roomDescriptions için)
    public void addImageUrl(String url) { this.imageUrls.add(url); }
    public void removeImageUrl(String url) { this.imageUrls.remove(url); }
    public void addRoomDescription(String description) { this.roomDescriptions.add(description); }
    public void removeRoomDescription(String description) { this.roomDescriptions.remove(description); }
    public void clearRoomDescriptions() { this.roomDescriptions.clear(); }
    // Diğer listeler için de benzer helper'lar eklenebilir (örneğin addPriceInfo...)
}