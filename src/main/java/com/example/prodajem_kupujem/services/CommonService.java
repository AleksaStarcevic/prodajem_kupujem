package com.example.prodajem_kupujem.services;


import com.example.prodajem_kupujem.entities.AdvertisementCategory;
import com.example.prodajem_kupujem.repositories.AdvertisementCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final AdvertisementCategoryRepository advertisementCategoryRepository;


    public List<AdvertisementCategory> getAllCategories() {
      return advertisementCategoryRepository.findAll();
    }
}
