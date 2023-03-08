package com.example.prodajem_kupujem.services;

import com.example.prodajem_kupujem.dto.mappers.UserResponseMapper;
import com.example.prodajem_kupujem.dto.users.UserCreditDTO;
import com.example.prodajem_kupujem.dto.users.UserResponseDTO;
import com.example.prodajem_kupujem.entities.Advertisement;
import com.example.prodajem_kupujem.entities.AdvertisementPromotion;
import com.example.prodajem_kupujem.entities.AppUser;
import com.example.prodajem_kupujem.exceptions.AdvertisementNotFoundException;
import com.example.prodajem_kupujem.exceptions.AdvertisementPromotionNotFoundException;
import com.example.prodajem_kupujem.exceptions.UserNotEnoughCreditException;
import com.example.prodajem_kupujem.exceptions.UserNotFoundException;
import com.example.prodajem_kupujem.repositories.AdvertisementPromotionRepository;
import com.example.prodajem_kupujem.repositories.AdvertisementRepository;
import com.example.prodajem_kupujem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static com.example.prodajem_kupujem.config.Constants.ADVERTISEMENT_PROMOTION_RESTORE;
import static com.example.prodajem_kupujem.config.Constants.ADVERTISEMENT_PROMOTION_STANDARD;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserResponseMapper userResponseMapper;

    private final AdvertisementPromotionRepository advertisementPromotionRepository;

    private final AdvertisementRepository advertisementRepository;

    public UserResponseDTO payCredit(UserCreditDTO dto, String email) throws UserNotFoundException {
        Optional<AppUser> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()) throw new UserNotFoundException("No user with given email");

        AppUser user = userOptional.get();
        user.setCredit(user.getCredit() + dto.getCredit());
        return userResponseMapper.apply(userRepository.save(user));

    }

    public void activatePromotion(int promotionID,int advertisementId,String email) throws UserNotFoundException, AdvertisementPromotionNotFoundException, AdvertisementNotFoundException, UserNotEnoughCreditException {
        Optional<AppUser> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()) throw new UserNotFoundException("No user with given email");
        AppUser user = userOptional.get();

        Optional<AdvertisementPromotion> promotionOptional = advertisementPromotionRepository.findById(promotionID);
        if(promotionOptional.isEmpty()) throw new AdvertisementPromotionNotFoundException("No promotion with given id");

        Optional<Advertisement> optionalAdvertisement = advertisementRepository.findByIdAndAppUser_Id(advertisementId,user.getId());
        if(optionalAdvertisement.isEmpty()) throw new AdvertisementNotFoundException("You can activate promotion only for your advertisement");

        AdvertisementPromotion promotion = promotionOptional.get();
        Advertisement advertisement = optionalAdvertisement.get();
        double userCredit = user.getCredit();
        double promotionPrice = promotion.getPrice();

        if(userCredit < promotionPrice) throw new UserNotEnoughCreditException("Not enough credit to activate this promotion");

        user.setCredit(userCredit - promotionPrice);

        if(promotionID == ADVERTISEMENT_PROMOTION_RESTORE){
            advertisement.setCreationDate(new Date());
            advertisement.setPromotionExpiration(null);
            advertisement.setAdvertisementPromotion(AdvertisementPromotion.builder().id(ADVERTISEMENT_PROMOTION_STANDARD).build());
        }else{
            advertisement.setPromotionExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(promotion.getDuration())));
            advertisement.setAdvertisementPromotion(AdvertisementPromotion.builder().id(promotionID).build());
        }
        advertisementRepository.save(advertisement);


    }



}
