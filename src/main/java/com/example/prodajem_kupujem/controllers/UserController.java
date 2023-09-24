package com.example.prodajem_kupujem.controllers;

import com.example.prodajem_kupujem.dto.advertisements.AdvertisementRatingDTO;
import com.example.prodajem_kupujem.dto.users.ActivatePromotionDTO;
import com.example.prodajem_kupujem.dto.users.EditAccountDto;
import com.example.prodajem_kupujem.dto.users.UserCreditDTO;
import com.example.prodajem_kupujem.dto.users.UserResponseDTO;
import com.example.prodajem_kupujem.exceptions.*;
import com.example.prodajem_kupujem.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.prodajem_kupujem.config.Constants.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
@SecurityRequirement(name = "prodajem_kupujem_api")
public class UserController {

    private final UserService userService;

    @PostMapping("/my_account/credit")
    public ResponseEntity<?> payCredit(@Valid @RequestBody UserCreditDTO dto, Authentication authentication) throws UserNotFoundException {
       return new ResponseEntity<>(userService.payCredit(dto,authentication.getName()), HttpStatus.OK);
    }

    @PostMapping("/my_account/promotion")
    public ResponseEntity<?> activatePromotion(@Valid @RequestBody ActivatePromotionDTO dto,Authentication authentication) throws UserNotFoundException, AdvertisementPromotionNotFoundException, AdvertisementNotFoundException, UserNotEnoughCreditException {
        userService.activatePromotion(dto.getPromotionID(),dto.getAdvertisementId(),authentication.getName());
        return new ResponseEntity<>("Promotion successfully activated", HttpStatus.OK);
    }

    @GetMapping("/my_account/advertisements")
    public ResponseEntity<?> getMyAdvertisements(@RequestParam(defaultValue = ADVERTISEMENT_STATUS_ACTIVE) String status,
                                                                                @RequestParam(value = "sort",defaultValue = ADVERTISEMENT_SORT_DATE_NEWEST) String[] sort,
                                                                                @RequestParam(value = "page",defaultValue = "1") int page, Authentication authentication){

        if(!AVAILABLE_SORTS.contains(sort[0]+","+sort[1])) return new ResponseEntity<>("Bad sort parameter",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userService.getMyAdvertisements(status,sort,page,authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("user/{id}/advertisements")
    public ResponseEntity<?> getUserAdvertisements(@RequestParam(value = "category",required = false) Integer category,
                                                   @RequestParam(value = "sort",defaultValue = ADVERTISEMENT_SORT_DATE_NEWEST) String[] sort,
                                                   @RequestParam(value = "page",defaultValue = "1") int page,
                                                   @PathVariable(value = "id") Integer userId){

        if(!AVAILABLE_SORTS.contains(sort[0]+","+sort[1])) return new ResponseEntity<>("Bad sort parameter",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userService.getUserAdvertisements(category,sort,page,userId), HttpStatus.OK);
    }

    @GetMapping("my_account/following")
    public ResponseEntity<?> getAdvertisementsThatIFollow(@RequestParam(value = "category",required = false) Integer category,
                                                          @RequestParam(value = "sort",defaultValue = ADVERTISEMENT_SORT_DATE_NEWEST) String[] sort,
                                                          @RequestParam(value = "page",defaultValue = "1") int page,
                                                          Authentication authentication) throws UserNotFoundException {

        return new ResponseEntity<>(userService.getAdvertisementsThatIFollow(category,sort,page,authentication.getName()), HttpStatus.OK);
    }

    @PostMapping("/user/{userId}/advertisements/{adId}/rating")
    public ResponseEntity<?> rateUserForAdvertisement(@PathVariable(value = "userId") int userId,
                                                      @PathVariable(value = "adId") int adId,
                                                      @Valid @RequestBody AdvertisementRatingDTO advertisementRatingDTO,
                                                      Authentication authentication) throws AdvertisementNotFoundException, UserNotFoundException, AdvertisementAlreadyRatedException {
        return new ResponseEntity<>(userService.rateUserForAdvertisement(userId,adId,advertisementRatingDTO,authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("/my_account/ratedAdvertisements")
    public ResponseEntity<?> getMyRatedAdvertisements(@RequestParam(value = "rate",defaultValue = RATE_POSITIVE) String rate,Authentication authentication){
        if(!AVAILABLE_RATES.contains(rate)) return new ResponseEntity<>("Bad rate parameter",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userService.getMyRatedAdvertisements(rate,authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/ratedAdvertisements")
    public ResponseEntity<?> getRatedAdvertisementsForUser(@PathVariable(value = "userId") int userId,
                                                           @RequestParam(value = "rate",defaultValue = RATE_POSITIVE) String rate,Authentication authentication) throws UserNotFoundException {

        if(!AVAILABLE_RATES.contains(rate)) return new ResponseEntity<>("Bad rate parameter",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userService.getRatedAdvertisementsForUser(userId,rate), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/likesNumber")
    public ResponseEntity<?> getRatedAdvertisementsForUser(@PathVariable(value = "userId") int userId) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getNumberOfLikesAndDislikes(userId), HttpStatus.OK);
    }

    @GetMapping("/my_account/ratedAdvertisements/likesNumber")
    public ResponseEntity<?> getRatedAdvertisementsForUser(Authentication authentication) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getMyNumberOfLikesAndDislikes(authentication.getName()), HttpStatus.OK);
    }

    @PatchMapping("/my_account")
    public ResponseEntity<UserResponseDTO> editMyAccount(Authentication authentication, @RequestBody EditAccountDto editAccountDto) throws UserNotFoundException {
        return new ResponseEntity<>(userService.editMyAccount(authentication.getName(),editAccountDto), HttpStatus.OK);
    }

    @GetMapping("/my_account")
    public ResponseEntity<UserResponseDTO> getMyInformation(Authentication authentication) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getMyInformation(authentication.getName()), HttpStatus.OK);
    }







}
