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
            "WHERE a.status_id IN (1,3);",nativeQuery = true)
    void updateStatuses();


    List<Advertisement> findAdvertisementsByAdvertisementStatus_StatusName(String name);


    List<Advertisement> findAdvertisementsByAdvertisementStatus_StatusNameOrAdvertisementStatus_StatusName(String status1,String status2);
    Optional<Advertisement> findById(int id);
    @Query("SELECT a from Advertisement a where a.id = :id and a.advertisementStatus.statusName <> :status")
    Optional<Advertisement> findByIdAndStatus(int id,String status);

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
    @Query("select a " +
            "from Advertisement a join AdvertisementCategory ac on a.advertisementCategory.id = ac.id " +
            "where ac.categoryName = :categoryName " +
            "order by a.advertisementPromotion.id desc,a.creationDate desc")
    List<Advertisement> findAdvertisementsFromCategoryAndOrderByPromotion(String categoryName);

    List<Advertisement> findAdvertisementsByTitleContaining(String keywords);

    @Query("select a " +
            "from Advertisement a join AdvertisementCategory ac on a.advertisementCategory.id = ac.id " +
            "where ac.categoryName = :categoryName " +
            "order by a.price desc")
    List<Advertisement> getAllAdsByCategorySortPriceDesc(String categoryName);

    List<Advertisement> findAdvertisementsByAdvertisementCategory_CategoryNameOrderByPriceAsc(String categoryName);

    List<Advertisement> findAdvertisementsByAdvertisementCategory_CategoryNameOrderByCreationDateDesc(String categoryName);

    Optional<Advertisement> findByIdAndAppUser_Id(int adId,int userId);

    @Transactional
    @Modifying
    @Query(value = "update Advertisement a " +
            "set a.promotionExpiration=null, a.advertisementPromotion.id = 1 " +
            "where CURRENT_DATE > a.promotionExpiration")
    void updateAdvertisementsPromotions();

}
