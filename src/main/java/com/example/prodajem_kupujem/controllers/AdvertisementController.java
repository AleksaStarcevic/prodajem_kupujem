package com.example.prodajem_kupujem.controllers;

import com.example.prodajem_kupujem.entities.Advertisement;
import com.example.prodajem_kupujem.services.AdvertisementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @PostMapping("")
    public void addAdvertisement(@RequestBody Advertisement advertisement){
        advertisementService.addAdvertisement(advertisement);
        System.out.println("aa");
    }

    @GetMapping("")
    public List<Advertisement> getAllAdvertisements(){
      return advertisementService.getAllAdvertisements();
    }
}
