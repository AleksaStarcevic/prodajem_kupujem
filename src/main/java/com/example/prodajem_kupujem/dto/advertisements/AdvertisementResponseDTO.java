package com.example.prodajem_kupujem.dto.advertisements;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisementResponseDTO {

    private String title;

    private String description;

    private byte[] picture;

    private double price;

    private String userEmail;

    private String advertisementCategory;

    private String advertisementStatus;

    private String advertisementPromotion;
}
