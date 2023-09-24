package com.example.prodajem_kupujem.dto.advertisements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementAddDTO {

    @NotEmpty(message = "Title cannot be null or empty")
    @Size(min = 3,max = 30,message = "Title must be between 3 and 30 characters")
    private String title;

    @NotEmpty(message = "Description cannot be null or empty")
    @Size(min = 3,max = 100,message = "Title must be between 3 and 100 characters")
    private String description;

    @NotEmpty(message = "Picture cannot be null or empty")
    private String picture;

    @Positive(message = "Price must have a positive value")
    @Min(value = 1,message = "Minimal price is 1")
    private double price;

    @Positive(message = "Category must have a positive value")
    private int advertisementCategory;

    @Positive(message = "Promotion must have a positive value")
    private int advertisementPromotion;
}
