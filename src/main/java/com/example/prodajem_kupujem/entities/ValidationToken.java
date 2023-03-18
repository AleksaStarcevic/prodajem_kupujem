package com.example.prodajem_kupujem.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ValidationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private Date generationTime;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
}
