package com.example.prodajem_kupujem.dto.users;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResponseDTO {
    Integer id;
    String name;
    String phone;
    String email;
    String city;
    double credit;

    public UserResponseDTO( String name, String city,String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.credit = credit;
    }

    public UserResponseDTO(Integer id, String name, String phone, String email, String city, double credit) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.credit = credit;
    }
}
