package com.example.prodajem_kupujem.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    private String email;

    private String name;

    private String city;

    private String phone;

    private double credit;


    @ManyToOne
    @JoinColumn(name = "role_id")
    private UserRole role;
}
