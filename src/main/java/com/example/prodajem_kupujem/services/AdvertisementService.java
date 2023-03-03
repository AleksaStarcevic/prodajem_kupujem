package com.example.prodajem_kupujem.services;

import com.example.prodajem_kupujem.dto.advertisements.AdvertisementAddDTO;
import com.example.prodajem_kupujem.dto.advertisements.AdvertisementPatchDTO;
import com.example.prodajem_kupujem.dto.advertisements.AdvertisementResponseDTO;
import com.example.prodajem_kupujem.entities.*;
import com.example.prodajem_kupujem.exceptions.AdvertisementNotFoundException;
import com.example.prodajem_kupujem.repositories.AdvertisementRepository;
import com.example.prodajem_kupujem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.prodajem_kupujem.config.Constants.ADVERTISEMENT_STATUS_ACTIVE;
import static com.example.prodajem_kupujem.config.Constants.ADVERTISEMENT_STATUS_EXPIRES;

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
                .creationDate(dto.getCreationDate())
                .appUser(userRepository.findByEmail(dto.getUserEmail()).get())
                .advertisementCategory(AdvertisementCategory.builder().id(dto.getAdvertisementCategory()).build())
                .advertisementStatus(AdvertisementStatus.builder().id(dto.getAdvertisementStatus()).build())
                .advertisementPromotion(AdvertisementPromotion.builder().id(dto.getAdvertisementPromotion()).build())
                .build();

      return advertisementRepository.save(ad);
    }

    public List<AdvertisementResponseDTO> getAllAdvertisements(String category) {
        return  advertisementRepository.findAdvertisementsByAdvertisementCategory_CategoryName(category).stream().map(ad ->
                                                            AdvertisementResponseDTO.builder().title(ad.getTitle())
                                                                    .description(ad.getDescription())
                                                                    .picture(ad.getPicture())
                                                                    .price(ad.getPrice())
                                                                    .creationDate(ad.getCreationDate())
                                                                    .user(new AppUser(ad.getAppUser().getEmail(), ad.getAppUser().getName(),ad.getAppUser().getCity(),ad.getAppUser().getPhone()))
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

    public List<Advertisement> getAdsByStatus(String status) {
        advertisementRepository.updateStatuses();
        List<Advertisement> ads;

        if(status.equals(ADVERTISEMENT_STATUS_ACTIVE)){
          ads =  advertisementRepository.findAdvertisementsByAdvertisementStatus_StatusNameOrAdvertisementStatus_StatusName(status,ADVERTISEMENT_STATUS_EXPIRES);
        }else{
           ads = advertisementRepository.findAdvertisementsByAdvertisementStatus_StatusName(status);
        }

        return ads;
    }


    public void advertisementPatch(int id,AdvertisementPatchDTO dto) throws AdvertisementNotFoundException {
       advertisementRepository.findById(id).orElseThrow(()-> new AdvertisementNotFoundException("No advertisement with given id"));
        advertisementRepository.patchAdvertisement(id,dto.getTitle(),dto.getDescription(),dto.getPicture(),dto.getPrice(),dto.getAdvertisementCategory(),dto.getAdvertisementPromotion(),dto.getAdvertisementStatus());
    }
}
