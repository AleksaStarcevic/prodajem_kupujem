package com.example.prodajem_kupujem.dto.advertisements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

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

    @NotNull(message = "Price cannot be null or empty")
    @Positive(message = "Price must have a positive value")
    @Min(value = 100,message = "Minimal price is 100")
    private double price;


    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Email should be valid")
    private String userEmail;

    @NotNull(message = "Category cannot be null or empty")
    @Positive(message = "Category must have a positive value")
    private int advertisementCategory;


    private int advertisementPromotion;
}
