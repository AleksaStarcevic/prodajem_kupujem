package com.example.prodajem_kupujem.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private double credit;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private UserRole role;


    public AppUser(String email, String name, String city, String phone) {
        this.email = email;
        this.name = name;
        this.city = city;
        this.phone = phone;
    }
}
