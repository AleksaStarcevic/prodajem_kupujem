package com.example.prodajem_kupujem.dto.advertisements;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class MyRatingsResponseDTO {

    private String description;

    private Boolean satisfied;

    private Date date;

    private String advertisementTitle;

    private String userName;

}
