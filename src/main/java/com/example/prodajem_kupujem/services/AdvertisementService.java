package com.example.prodajem_kupujem.services;

import com.example.prodajem_kupujem.dto.advertisements.AdvertisementAddDTO;
import com.example.prodajem_kupujem.dto.advertisements.AdvertisementResponseDTO;
import com.example.prodajem_kupujem.entities.Advertisement;
import com.example.prodajem_kupujem.entities.AdvertisementCategory;
import com.example.prodajem_kupujem.entities.AdvertisementPromotion;
import com.example.prodajem_kupujem.entities.AdvertisementStatus;
import com.example.prodajem_kupujem.repositories.AdvertisementRepository;
import com.example.prodajem_kupujem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;


    public Advertisement addAdvertisement(AdvertisementAddDTO dto){
        String base64 = dto.getPicture();
        byte[] pic = Base64.getDecoder().decode(base64);

        Advertisement ad = Advertisement.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .picture(pic)
                .price(dto.getPrice())
                .appUser(userRepository.findByEmail(dto.getUserEmail()).get())
                .advertisementCategory(AdvertisementCategory.builder().id(dto.getAdvertisementCategory()).build())
                .advertisementStatus(AdvertisementStatus.builder().id(dto.getAdvertisementStatus()).build())
                .advertisementPromotion(AdvertisementPromotion.builder().id(dto.getAdvertisementPromotion()).build())
                .build();

      return advertisementRepository.save(ad);
    }

    public List<AdvertisementResponseDTO> getAllAdvertisements() {
        return  advertisementRepository.findAll().stream().map(ad ->
                                                            AdvertisementResponseDTO.builder().title(ad.getTitle())
                                                                    .description(ad.getDescription())
                                                                    .picture(ad.getPicture())
                                                                    .price(ad.getPrice())
                                                                    .userEmail(ad.getAppUser().getEmail())
                                                                    .advertisementCategory(ad.getAdvertisementCategory().getCategoryName())
                                                                    .advertisementStatus(ad.getAdvertisementStatus().getStatusName())
                                                                    .advertisementPromotion(ad.getAdvertisementPromotion().getTitle())
                                                                    .build()
                                                            ).collect(Collectors.toList());
    }

    public byte[] getAdPic(int id) {
      Advertisement advertisement = advertisementRepository.getById(id);
        return advertisement.getPicture();
    }
}
