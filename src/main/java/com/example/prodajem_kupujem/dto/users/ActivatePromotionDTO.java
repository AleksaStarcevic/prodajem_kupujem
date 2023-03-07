package com.example.prodajem_kupujem.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivatePromotionDTO {


    @Positive(message = "Promotion id must be a positive number")
    private int promotionID;
    @Positive(message = "Advertisement id must be a positive number")
    private int advertisementId;
}
