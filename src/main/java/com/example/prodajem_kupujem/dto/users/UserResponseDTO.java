package com.example.prodajem_kupujem.dto.users;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResponseDTO {

    String name;
    String phone;
    String email;
    String city;
    double credit;
}
