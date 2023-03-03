package com.example.prodajem_kupujem.repositories;

import com.example.prodajem_kupujem.entities.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement,Integer> {
    @Transactional
    @Modifying
    @Query(value = "update advertisement a set a.status_id = " +
            "(CASE WHEN datediff(curdate(),a.creation_date) BETWEEN 25 AND 30 THEN 3 " +
            "WHEN datediff(curdate(),a.creation_date) > 30 THEN 2 " +
            "ELSE 1 " +
            "END) " +
            "WHERE a.status_id = 1 OR a.status_id=3;",nativeQuery = true)
    void updateStatuses();


    List<Advertisement> findAdvertisementsByAdvertisementStatus_StatusName(String name);


    List<Advertisement> findAdvertisementsByAdvertisementStatus_StatusNameOrAdvertisementStatus_StatusName(String status1,String status2);

    Optional<Advertisement> findById(int id);

    @Transactional
    @Modifying
    @Query(value ="UPDATE Advertisement a " +
            "SET a.title = :title, " +
            "a.description = :description, " +
            "a.picture = :picture, " +
            "a.price = :price, " +
            "a.advertisementCategory.id= :advertisementCategory, " +
            "a.advertisementStatus.id = :advertisementStatus, " +
            "a.advertisementPromotion.id = :advertisementPromotion " +
            "WHERE a.id = :id")
    void patchAdvertisement(@Param("id")int id, @Param("title")String title,@Param("description") String description,@Param("picture") String picture,@Param("price") double price,@Param("advertisementCategory") int advertisementCategory, @Param("advertisementPromotion")int advertisementPromotion,@Param("advertisementStatus") int advertisementStatus);

    List<Advertisement> findAdvertisementsByAdvertisementCategory_CategoryName(String categoryName);
}
