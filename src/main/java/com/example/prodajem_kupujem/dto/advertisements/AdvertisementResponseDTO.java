package com.example.prodajem_kupujem.dto.advertisements;

import com.example.prodajem_kupujem.entities.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisementResponseDTO {

    private String title;

    private String description;

    private byte[] picture;

    private double price;

    private Date creationDate;

    private AppUser user;

    private String advertisementCategory;

    private String advertisementStatus;

    private String advertisementPromotion;
}
