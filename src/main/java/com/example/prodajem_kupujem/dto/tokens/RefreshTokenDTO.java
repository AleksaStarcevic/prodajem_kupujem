package com.example.prodajem_kupujem.dto.tokens;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDTO {

    private String accessToken;
    private String refreshToken;
}
