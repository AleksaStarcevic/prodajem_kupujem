package com.example.prodajem_kupujem.dto.users;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class UserRegistrationDTO {

    @Email(message = "Email must be valid")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{4,}$",message = "Password length must be min 4, it must contain at least one upperCase letter, special character and number")
    private String password;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "City cannot be empty")
    private String city;

    @NotEmpty(message = "Phone cannot be empty")
    @Pattern(regexp = "^(\\d{3}-?){2}\\d{4}$",message = "Phone must be in format 123-456-7890")
    private String phone;
}
