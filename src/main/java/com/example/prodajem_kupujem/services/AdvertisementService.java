package com.example.prodajem_kupujem.services;

import com.example.prodajem_kupujem.dto.advertisements.AdvertisementAddDTO;
import com.example.prodajem_kupujem.dto.advertisements.AdvertisementPatchDTO;
import com.example.prodajem_kupujem.dto.advertisements.AdvertisementResponseDTO;
import com.example.prodajem_kupujem.dto.mappers.AdvertisementResponseMapper;
import com.example.prodajem_kupujem.entities.*;
import com.example.prodajem_kupujem.exceptions.AdvertisementNotFoundException;
import com.example.prodajem_kupujem.exceptions.AdvertisementPromotionNotFoundException;
import com.example.prodajem_kupujem.exceptions.UserNotEnoughCreditException;
import com.example.prodajem_kupujem.exceptions.UserNotFoundException;
import com.example.prodajem_kupujem.repositories.AdvertisementRepository;
import com.example.prodajem_kupujem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.prodajem_kupujem.config.Constants.*;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    private final AdvertisementResponseMapper advertisementResponseMapper;


    public Advertisement addAdvertisement(AdvertisementAddDTO dto,String email) throws UserNotFoundException, UserNotEnoughCreditException, AdvertisementNotFoundException, AdvertisementPromotionNotFoundException {
        String base64 = dto.getPicture();
        byte[] pic = Base64.getDecoder().decode(base64);

        Advertisement ad = Advertisement.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .picture(pic)
                .price(dto.getPrice())
                .creationDate(new Date())
                .appUser(userRepository.findByEmail(dto.getUserEmail()).get())
                .advertisementCategory(AdvertisementCategory.builder().id(dto.getAdvertisementCategory()).build())
                .advertisementStatus(AdvertisementStatus.builder().id(ADVERTISEMENT_STATUS_ACTIVE_).build())
                .advertisementPromotion(AdvertisementPromotion.builder().id(dto.getAdvertisementPromotion()).build())
                .build();

        Advertisement advertisement =  advertisementRepository.save(ad);
        if(advertisement.getAdvertisementPromotion().getId() != ADVERTISEMENT_PROMOTION_STANDARD){
            userService.activatePromotion(dto.getAdvertisementPromotion(),advertisement.getId(),email);
        }
        return advertisement;
    }

    public List<AdvertisementResponseDTO> getAllAdvertisements(String category,int page) {
        List<Advertisement> advertisementList = advertisementRepository.findAdvertisementsFromCategoryAndOrderByPromotion(category, PageRequest.of(page-1,PAGE_SIZE)).getContent();
        return   advertisementList.stream()
                .map(ad -> advertisementResponseMapper.apply(ad))
                .collect(Collectors.toList());
    }

    public byte[] getAdPic(int id) {
      Advertisement advertisement = advertisementRepository.getById(id);
        return advertisement.getPicture();
    }

    public List<Advertisement> getAdsByStatus(String status) {
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

    public void followAdvertisement(int id, String userEmail) throws UserNotFoundException, AdvertisementNotFoundException {

        Optional<AppUser> optUser = userRepository.findByEmail(userEmail);
        Optional<Advertisement> optAd = advertisementRepository.findByIdAndStatus(id,ADVERTISEMENT_STATUS_EXPIRED);

        if(optUser.isEmpty()) throw new UserNotFoundException("No user with given email");
        if(optAd.isEmpty()) throw new AdvertisementNotFoundException("Cant follow expired advertisement");

        AppUser appUser = optUser.get();
        Advertisement ad = optAd.get();
        ad.addFollowers(appUser);
        advertisementRepository.save(ad);
    }

    public void unfollowAdvertisement(int id, String email) throws UserNotFoundException, AdvertisementNotFoundException {
        Optional<AppUser> optUser = userRepository.findByEmail(email);
        Optional<Advertisement> optAd = advertisementRepository.findById(id);

        if(optUser.isEmpty()) throw new UserNotFoundException("No user with given email");
        if(optAd.isEmpty()) throw new AdvertisementNotFoundException("Bad advertisement id");

        AppUser appUser = optUser.get();
        Advertisement ad = optAd.get();
        ad.removeFollower(appUser.getId());
        advertisementRepository.save(ad);

    }

    public long getAdFollowers(int id) throws AdvertisementNotFoundException {
        Optional<Advertisement> optAd = advertisementRepository.findById(id);
        if(optAd.isEmpty()) throw new AdvertisementNotFoundException("Bad advertisement id");
        Advertisement ad = optAd.get();
        long followers = ad.getFollowers().stream().count();
        return followers;
    }

    public List<Advertisement> searchAdvertisements(String keywords) {
     return advertisementRepository.findAdvertisementsByTitleContaining(keywords);
    }

    public List<AdvertisementResponseDTO> sortAdvertisements(String category, String[] sort, int page) {
        List<Sort.Order> orders = new ArrayList<>();
        if(sort[0].contains(",")){
            for (String s : sort) {
                String[] fields = s.split(",");
                orders.add(new Sort.Order(getSortOrder(fields[1]),fields[0]));
            }
        }else{
            orders.add(new Sort.Order(getSortOrder(sort[1]),sort[0]));
        }

        Pageable pageable = PageRequest.of(page-1,PAGE_SIZE,Sort.by(orders));
        Date dateOfActiveAd = Date.from(LocalDate.now().minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Advertisement> advertisements = advertisementRepository.findAdvertisementsByAdvertisementCategory_CategoryName(category,dateOfActiveAd ,pageable).getContent();
        return advertisements.stream().map(advertisementResponseMapper::apply).collect(Collectors.toList());
    }

    private Sort.Direction getSortOrder(String direction) {
        if(direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }
}
