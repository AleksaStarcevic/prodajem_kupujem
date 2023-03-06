package com.example.prodajem_kupujem.controllers;

import com.example.prodajem_kupujem.dto.advertisements.AdvertisementAddDTO;
import com.example.prodajem_kupujem.dto.advertisements.AdvertisementPatchDTO;
import com.example.prodajem_kupujem.dto.advertisements.AdvertisementResponseDTO;
import com.example.prodajem_kupujem.entities.Advertisement;
import com.example.prodajem_kupujem.exceptions.AdvertisementNotFoundException;
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

import static com.example.prodajem_kupujem.config.Constants.*;

@RestController
@RequestMapping("/advertisements")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;


    @PostMapping("")
    public ResponseEntity<?> addAdvertisement(@Valid @RequestBody AdvertisementAddDTO dto){
        advertisementService.addAdvertisement(dto);
        return new ResponseEntity<>("Advertisement successfully created", HttpStatus.CREATED);
    }

    @GetMapping("/category/{category}")
    public List<AdvertisementResponseDTO> getAllAdvertisementsFromCategory(@PathVariable String category) {
      return advertisementService.getAllAdvertisements(category);
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

    @GetMapping(value = "/category/{category}/order")
    public ResponseEntity<?> sortAdvertisementsFromCategory(@PathVariable String category, @RequestParam String sort){
        switch (sort){
            case ADVERTISEMENT_SORT_PRICE_DESCENDING :
                return new ResponseEntity<>(advertisementService.sortAdvertisementsByPriceDesc(category),HttpStatus.OK);
            case ADVERTISEMENT_SORT_PRICE_ASCENDING:
                return new ResponseEntity<>(advertisementService.sortAdvertisementsByPriceAsc(category),HttpStatus.OK);
            case ADVERTISEMENT_SORT_DATE_NEWEST:
                return new ResponseEntity<>(advertisementService.sortAdvertisementsByNewest(category),HttpStatus.OK);
        }
        return new ResponseEntity<>("Bad request param",HttpStatus.BAD_REQUEST);
    }
}
