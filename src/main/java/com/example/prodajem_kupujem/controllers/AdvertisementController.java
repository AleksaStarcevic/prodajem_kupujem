package com.example.prodajem_kupujem.controllers;

import com.example.prodajem_kupujem.dto.advertisements.AdvertisementAddDTO;
import com.example.prodajem_kupujem.dto.advertisements.AdvertisementPatchDTO;
import com.example.prodajem_kupujem.dto.advertisements.AdvertisementResponseDTO;
import com.example.prodajem_kupujem.entities.Advertisement;
import com.example.prodajem_kupujem.exceptions.AdvertisementNotFoundException;
import com.example.prodajem_kupujem.exceptions.AdvertisementPromotionNotFoundException;
import com.example.prodajem_kupujem.exceptions.UserNotEnoughCreditException;
import com.example.prodajem_kupujem.exceptions.UserNotFoundException;
import com.example.prodajem_kupujem.services.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.prodajem_kupujem.config.Constants.ADVERTISEMENT_SORT_DATE_NEWEST;
import static com.example.prodajem_kupujem.config.Constants.ADVERTISEMENT_SORT_PROMOTION;

@RestController
@RequestMapping("/advertisements")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;


    @PostMapping("")
    public ResponseEntity<?> addAdvertisement(@Valid @RequestBody AdvertisementAddDTO dto,Authentication authentication) throws UserNotFoundException, UserNotEnoughCreditException, AdvertisementNotFoundException, AdvertisementPromotionNotFoundException {
        return new ResponseEntity<>( advertisementService.addAdvertisement(dto,authentication.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/category/{category}/search")
    public List<AdvertisementResponseDTO> getAllAdvertisementsFromCategory(@PathVariable String category,
                                                                           @RequestParam(value = "sort",required = false) String[] sort,
                                                                           @RequestParam(value = "page",defaultValue = "1") int page) {
       if(sort == null){
           sort = new String[]{ADVERTISEMENT_SORT_PROMOTION, ADVERTISEMENT_SORT_DATE_NEWEST};
       }
       return advertisementService.sortAdvertisements(category,sort,page);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getImageFromAd(@PathVariable int id){
      byte[] image = advertisementService.getAdPic(id);
      return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(image);
    }

    @GetMapping("/my-ads")
    public ResponseEntity<?> getAdvertisementsByStatus(@RequestParam String status){
      return new ResponseEntity<>(advertisementService.getAdsByStatus(status),HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public void patchAdvertisement(@PathVariable int id, @RequestBody @Valid AdvertisementPatchDTO dto) throws AdvertisementNotFoundException {
        advertisementService.advertisementPatch(id,dto);
    }

    @PatchMapping("/{id}/follow")
    public void followAdvertisement(@PathVariable int id, Authentication authentication) throws UserNotFoundException, AdvertisementNotFoundException {
        advertisementService.followAdvertisement(id,authentication.getName());
    }

    @PatchMapping("/{id}/unfollow")
    public void unfollowAdvertisement(@PathVariable int id, Authentication authentication) throws UserNotFoundException, AdvertisementNotFoundException {
        advertisementService.unfollowAdvertisement(id,authentication.getName());
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<?> getFollowersForAdvertisement(@PathVariable int id) throws AdvertisementNotFoundException {
       return new ResponseEntity<>(advertisementService.getAdFollowers(id),HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAdvertisements(@RequestParam String keywords){
        List<Advertisement> ads = advertisementService.searchAdvertisements(keywords);
        if(ads.isEmpty()){
            return  new ResponseEntity<>("No advertisements found for search criteria",HttpStatus.OK);
        }
        return  new ResponseEntity<>(ads,HttpStatus.OK);
    }

}
