package com.example.prodajem_kupujem.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private String city;

    private String phone;

    @Column(columnDefinition = "double default 0.0")
    private double credit;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private UserRole role;


    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "followers")
    private  Set<Advertisement> following = new HashSet<>();

}
