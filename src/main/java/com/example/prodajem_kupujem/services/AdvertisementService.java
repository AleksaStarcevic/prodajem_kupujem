package com.example.prodajem_kupujem.services;

import com.example.prodajem_kupujem.entities.Advertisement;
import com.example.prodajem_kupujem.repositories.AdvertisementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    public AdvertisementService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    public void addAdvertisement(Advertisement advertisement){
        advertisementRepository.save(advertisement);
    }

    public List<Advertisement> getAllAdvertisements() {
        return advertisementRepository.findAll();
    }
}
