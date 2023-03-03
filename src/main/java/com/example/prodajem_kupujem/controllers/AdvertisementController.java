package com.example.prodajem_kupujem.controllers;

import com.example.prodajem_kupujem.dto.advertisements.AdvertisementAddDTO;
import com.example.prodajem_kupujem.dto.advertisements.AdvertisementPatchDTO;
import com.example.prodajem_kupujem.dto.advertisements.AdvertisementResponseDTO;
import com.example.prodajem_kupujem.exceptions.AdvertisementNotFoundException;
import com.example.prodajem_kupujem.services.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
}
