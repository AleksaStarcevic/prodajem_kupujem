package com.example.prodajem_kupujem.repositories;

import com.example.prodajem_kupujem.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser,String> {


    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findById(int id);


}
