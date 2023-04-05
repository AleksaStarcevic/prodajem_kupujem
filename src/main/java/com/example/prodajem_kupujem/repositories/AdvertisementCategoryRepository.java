package com.example.prodajem_kupujem.repositories;

import com.example.prodajem_kupujem.entities.AdvertisementCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvertisementCategoryRepository extends JpaRepository<AdvertisementCategory,Integer> {

    Optional<AdvertisementCategory> findById(int id);
}
