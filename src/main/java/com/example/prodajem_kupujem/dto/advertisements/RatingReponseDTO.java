package com.example.prodajem_kupujem.dto.advertisements;

import com.example.prodajem_kupujem.dto.users.UserResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class RatingReponseDTO {

    private String description;

    private Boolean satisfied;

    private Date date;

   private UserResponseDTO userThatRatedAdvertisement;

   private AdvertisementResponseDTO advertisement;
}
