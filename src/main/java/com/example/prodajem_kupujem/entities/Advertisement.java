package com.example.prodajem_kupujem.entities;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    @Lob
    private byte[] picture;

    private double price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private AdvertisementCategory advertisementCategory;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private AdvertisementStatus advertisementStatus;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private AdvertisementPromotion advertisementPromotion;

}
