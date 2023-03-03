package com.example.prodajem_kupujem.dto.advertisements;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementPatchDTO {

    @NotEmpty(message = "Title cannot be null or empty")
    @Size(min = 3,max = 30,message = "Title must be between 3 and 30 characters")
    private String title;

    @NotEmpty(message = "Description cannot be null or empty")
    @Size(min = 3,max = 100,message = "Title must be between 3 and 100 characters")
    private String description;


    private String picture;


    @Positive(message = "Price must have a positive value")
    @Min(value = 100,message = "Minimal price is 100")
    private double price;



    @Positive(message = "Category must have a positive value")
    private int advertisementCategory;



    @Positive(message = "Status must have a positive value")
    private int advertisementStatus;


    @Positive(message = "Promotion must have a positive value")
    private int advertisementPromotion;
}
