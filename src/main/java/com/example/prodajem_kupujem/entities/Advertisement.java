package com.example.prodajem_kupujem.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    @Lob
    private byte[] picture;

    private double price;

    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private AdvertisementCategory advertisementCategory;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private AdvertisementStatus advertisementStatus;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private AdvertisementPromotion advertisementPromotion;

    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(name = "followings",
            joinColumns = {@JoinColumn(name = "advertisement_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private  Set<AppUser> followers = new HashSet<>();

    public void addFollowers(AppUser user){
        this.followers.add(user);
        user.getFollowing().add(this);
    }

    public void removeFollower(int userId){
     Optional<AppUser> opt =  followers.stream()
                                .filter(u -> u.getId() == userId)
                                .findFirst();
     if(opt.isPresent()){
         AppUser user = opt.get();
         this.followers.remove(user);
         user.getFollowing().remove(this);
     }
    }




}
