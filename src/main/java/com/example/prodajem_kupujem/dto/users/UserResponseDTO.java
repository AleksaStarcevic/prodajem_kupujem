package com.example.prodajem_kupujem.dto.users;

import lombok.Builder;

@Builder
public class UserResponseDTO {

    String name;
    String phone;
    String email;
    String city;
}
