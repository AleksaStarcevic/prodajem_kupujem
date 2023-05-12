package com.example.prodajem_kupujem.dto.advertisements;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementNumberOfLikesResponseDTO {

    Integer likes;
    Integer dislikes;
}
