package com.example.prodajem_kupujem.dto.advertisements;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementPatchDTO {


    private String title;

    private String description;

    private String picture;

    private Double price;

    private Integer advertisementCategory;

}
