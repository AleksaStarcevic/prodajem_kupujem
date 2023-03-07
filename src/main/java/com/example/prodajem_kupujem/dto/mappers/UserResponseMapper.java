package com.example.prodajem_kupujem.dto.mappers;

import com.example.prodajem_kupujem.dto.users.UserResponseDTO;
import com.example.prodajem_kupujem.entities.AppUser;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserResponseMapper implements Function<AppUser, UserResponseDTO> {

    @Override
    public UserResponseDTO apply(AppUser user) {
       return UserResponseDTO.builder()
               .email(user.getEmail())
               .name(user.getName())
               .city(user.getCity())
               .phone(user.getPhone())
               .credit(user.getCredit())
               .build();
    }
}
