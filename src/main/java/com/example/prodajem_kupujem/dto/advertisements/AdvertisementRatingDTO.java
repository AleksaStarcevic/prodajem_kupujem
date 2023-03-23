package com.example.prodajem_kupujem.dto.advertisements;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AdvertisementRatingDTO {

    @NotEmpty(message = "Description cannot be null or empty")
    @Size(min = 5,max = 40,message = "Description must be between 5 and 40 characters")
    private String description;

    @NotNull(message = "Satisfaction cannot be null")
    private Boolean satisfied;
}
