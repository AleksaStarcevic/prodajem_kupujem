package com.example.prodajem_kupujem.repositories;

import com.example.prodajem_kupujem.entities.AdvertisementPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvertisementPromotionRepository extends JpaRepository<AdvertisementPromotion,Integer> {

    Optional<AdvertisementPromotion> findById(int id);
}
