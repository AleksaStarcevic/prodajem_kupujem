package com.example.prodajem_kupujem.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreditDTO {

    @Min(value = 100,message = "Min amount to deposit is 100")
    @Max(value = 10000,message = "Max amount to deposit is 10000")
    @Positive(message = "Credit must be a positive number")
    double credit;
}
