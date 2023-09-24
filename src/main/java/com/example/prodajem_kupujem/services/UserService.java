package com.example.prodajem_kupujem.services;

import com.example.prodajem_kupujem.dto.advertisements.*;
import com.example.prodajem_kupujem.dto.mappers.AdvertisementResponseMapper;
import com.example.prodajem_kupujem.dto.mappers.UserResponseMapper;
import com.example.prodajem_kupujem.dto.users.EditAccountDto;
import com.example.prodajem_kupujem.dto.users.UserCreditDTO;
import com.example.prodajem_kupujem.dto.users.UserResponseDTO;
import com.example.prodajem_kupujem.entities.Advertisement;
import com.example.prodajem_kupujem.entities.AdvertisementPromotion;
import com.example.prodajem_kupujem.entities.AppUser;
import com.example.prodajem_kupujem.entities.Rating;
import com.example.prodajem_kupujem.exceptions.*;
import com.example.prodajem_kupujem.repositories.AdvertisementPromotionRepository;
import com.example.prodajem_kupujem.repositories.AdvertisementRepository;
import com.example.prodajem_kupujem.repositories.RatingRepository;
import com.example.prodajem_kupujem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.prodajem_kupujem.config.Constants.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserResponseMapper userResponseMapper;

    private final AdvertisementPromotionRepository advertisementPromotionRepository;

    private final AdvertisementRepository advertisementRepository;

    private final AdvertisementResponseMapper advertisementResponseMapper;

    private final RatingRepository ratingRepository;

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


    public List<AdvertisementResponseDTO> getMyAdvertisements(String status, String[] sort, int page, String name) {
        Pageable pageable = getPageable(sort, page);
        List<Advertisement> advertisements = advertisementRepository.findByAppUser_EmailAndAdvertisementStatus_StatusName(name,status,pageable).getContent();
        return advertisements.stream().map(advertisementResponseMapper::apply).collect(Collectors.toList());
    }

    private Pageable getPageable(String[] sort, int page) {
        Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sort[0]);
        Pageable pageable = PageRequest.of(page -1,PAGE_SIZE,Sort.by(order));
        return pageable;
    }

    public List<AdvertisementResponseDTO> getUserAdvertisements(Integer category, String[] sort, int page, Integer userId) {
        Pageable pageable = getPageable(sort,page);
        List<Advertisement> advertisements;
        if(category != null){
            advertisements = advertisementRepository.findByAppUser_IdAndAdvertisementCategory_Id(userId,category,pageable).getContent();
        }else{
           advertisements = advertisementRepository.findByAppUser_Id(userId,pageable).getContent();
        }
        return advertisements.stream().map(advertisementResponseMapper::apply).collect(Collectors.toList());

    }

    public List<AdvertisementResponseDTO> getAdvertisementsThatIFollow(Integer category, String[] sort, int page, String email) throws UserNotFoundException {
        Pageable pageable = getPageable(sort,page);
        List<Advertisement> advertisements;
        if(category != null){
            advertisements = advertisementRepository.findAdvertisementsByAdvertisementCategory_IdAndFollowersEmail(category,email,pageable).getContent();
        }else{
            advertisements = advertisementRepository.findAdvertisementsByFollowersEmail(email,pageable).getContent();
        }
        return advertisements.stream().map(advertisementResponseMapper::apply).collect(Collectors.toList());

    }

    public RatingReponseDTO rateUserForAdvertisement(int userId, int adId, AdvertisementRatingDTO advertisementRatingDTO,String userEmail) throws AdvertisementNotFoundException, UserNotFoundException, AdvertisementAlreadyRatedException {
        AppUser appUser = userRepository.findByEmail(userEmail).orElseThrow(()-> new UserNotFoundException("User not found with given email"));
        if(appUser.getId() == userId) throw new UserNotFoundException("Cannot rate your own advertisement");
        Advertisement advertisement = advertisementRepository.findByIdAndAppUser_Id(adId,userId).orElseThrow(()-> new AdvertisementNotFoundException("There is no advertisement for this user"));
        if(advertisement.getAdvertisementStatus().getStatusName().equals(ADVERTISEMENT_STATUS_EXPIRED)) throw new AdvertisementNotFoundException("Cannot rate expired advertisement");
        Optional<Rating> ratingOptional = ratingRepository.findRatingByAdvertisement_IdAndAppUser_Id(adId,appUser.getId());
        if(ratingOptional.isPresent()) throw new AdvertisementAlreadyRatedException("Advertisement is already rated by user");

        Rating ratingToSave = Rating.builder()
                .advertisement(advertisement)
                .appUser(appUser)
                .date(new Date())
                .description(advertisementRatingDTO.getDescription())
                .satisfied(advertisementRatingDTO.getSatisfied())
                .build();

      Rating rating = ratingRepository.save(ratingToSave);
      AdvertisementResponseDTO advertisementResponseDTO = advertisementResponseMapper.apply(rating.getAdvertisement());
      UserResponseDTO userResponseDTO = userResponseMapper.apply(rating.getAppUser());

      return RatingReponseDTO.builder()
              .advertisement(advertisementResponseDTO)
              .userThatRatedAdvertisement(userResponseDTO)
              .date(rating.getDate())
              .description(rating.getDescription())
              .satisfied(rating.isSatisfied())
              .build();
    }

    public List<MyRatingsResponseDTO> getMyRatedAdvertisements(String rate, String userEmail) {
        List<Rating> ratings;
        if(rate.equals(RATE_POSITIVE)){
            ratings = ratingRepository.findRatingByAdvertisement_AppUser_EmailAndSatisfiedIsTrue(userEmail);
        }else{
            ratings = ratingRepository.findRatingByAdvertisement_AppUser_EmailAndSatisfiedIsFalse(userEmail);
        }
        return ratings.stream().map(rating -> MyRatingsResponseDTO.builder()
                                        .id(rating.getId())
                                      .advertisementTitle(rating.getAdvertisement().getTitle())
                                      .userName(rating.getAppUser().getName())
                                      .date(rating.getDate())
                                      .description(rating.getDescription())
                                      .satisfied(rating.isSatisfied())
                                      .build()).collect(Collectors.toList());
    }

    public List<MyRatingsResponseDTO> getRatedAdvertisementsForUser(int userId, String rate) throws UserNotFoundException {
        AppUser appUser = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found with given id"));
        return getMyRatedAdvertisements(rate,appUser.getEmail());
    }

    public AdvertisementNumberOfLikesResponseDTO getNumberOfLikesAndDislikes(int userId) throws UserNotFoundException {
        AppUser appUser = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found with given id"));
        Integer numOfDislikes = ratingRepository.countRatingByAdvertisement_AppUser_EmailAndSatisfiedIsFalse(appUser.getEmail());
        Integer numOfLikes = ratingRepository.countRatingByAdvertisement_AppUser_EmailAndSatisfiedIsTrue(appUser.getEmail());

        return AdvertisementNumberOfLikesResponseDTO.builder()
                .dislikes(numOfDislikes)
                .likes(numOfLikes)
                .build();
    }

    public AdvertisementNumberOfLikesResponseDTO getMyNumberOfLikesAndDislikes(String username) throws UserNotFoundException {
        AppUser appUser = userRepository.findByEmail(username).orElseThrow(()-> new UserNotFoundException("User not found with given email"));
        Integer numOfDislikes = ratingRepository.countRatingByAdvertisement_AppUser_EmailAndSatisfiedIsFalse(appUser.getEmail());
        Integer numOfLikes = ratingRepository.countRatingByAdvertisement_AppUser_EmailAndSatisfiedIsTrue(appUser.getEmail());

        return AdvertisementNumberOfLikesResponseDTO.builder()
                .dislikes(numOfDislikes)
                .likes(numOfLikes)
                .build();
    }

    public UserResponseDTO editMyAccount(String name, EditAccountDto editAccountDto) throws UserNotFoundException {
        AppUser appUser = userRepository.findByEmail(name).orElseThrow(()-> new UserNotFoundException("User not found with given email"));
        String dtoName = editAccountDto.getName();
        String dtoCity = editAccountDto.getCity();
        String dtoPhone = editAccountDto.getPhone();

        if(dtoName != null) appUser.setName(dtoName);
        if(dtoCity != null) appUser.setCity(dtoCity);
        if(dtoPhone != null) appUser.setPhone(dtoPhone);
        AppUser savedUser = userRepository.save(appUser);
        return new UserResponseDTO(savedUser.getName(),savedUser.getCity(),savedUser.getPhone());

    }

    public UserResponseDTO getMyInformation(String name) throws UserNotFoundException {
        AppUser appUser = userRepository.findByEmail(name).orElseThrow(()-> new UserNotFoundException("User not found with given email"));
        return new UserResponseDTO(appUser.getId(),appUser.getName(),appUser.getPhone(),appUser.getEmail(),appUser.getCity(),appUser.getCredit());
    }
}
