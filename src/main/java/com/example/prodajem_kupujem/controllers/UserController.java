package com.example.prodajem_kupujem.controllers;

import com.example.prodajem_kupujem.dto.users.ActivatePromotionDTO;
import com.example.prodajem_kupujem.dto.users.UserCreditDTO;
import com.example.prodajem_kupujem.exceptions.AdvertisementNotFoundException;
import com.example.prodajem_kupujem.exceptions.AdvertisementPromotionNotFoundException;
import com.example.prodajem_kupujem.exceptions.UserNotEnoughCreditException;
import com.example.prodajem_kupujem.exceptions.UserNotFoundException;
import com.example.prodajem_kupujem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/credit")
    public ResponseEntity<?> payCredit(@Valid @RequestBody UserCreditDTO dto, Authentication authentication) throws UserNotFoundException {
       return new ResponseEntity<>(userService.payCredit(dto,authentication.getName()), HttpStatus.OK);
    }

    @PostMapping("/promotion")
    public ResponseEntity<?> activatePromotion(@Valid @RequestBody ActivatePromotionDTO dto,Authentication authentication) throws UserNotFoundException, AdvertisementPromotionNotFoundException, AdvertisementNotFoundException, UserNotEnoughCreditException {
        userService.activatePromotion(dto.getPromotionID(),dto.getAdvertisementId(),authentication.getName());
        return new ResponseEntity<>("Promotion successfully activated", HttpStatus.OK);
    }


}
