package com.example.prodajem_kupujem.services;

import com.example.prodajem_kupujem.repositories.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final AdvertisementRepository advertisementRepository;

    @Scheduled(cron = "@daily")
    public void updateAdvertisementsStatus(){
        advertisementRepository.updateStatuses();
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void updateAdvertisementsPromotions(){
        advertisementRepository.updateAdvertisementsPromotions();
    }
}
