package com.example.prodajem_kupujem.repositories;

import com.example.prodajem_kupujem.entities.ValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidationTokenRepository extends JpaRepository<ValidationToken,Integer> {

    Optional<ValidationToken> findByToken(String token);

}