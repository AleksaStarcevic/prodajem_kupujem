package com.example.prodajem_kupujem.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.prodajem_kupujem.dto.RefreshTokenDTO;
import com.example.prodajem_kupujem.entities.AppUser;
import com.example.prodajem_kupujem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.prodajem_kupujem.config.Constants.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public RefreshTokenDTO sendRefreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
        try {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            String token = authorizationHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String username = decodedJWT.getSubject();
            Optional<AppUser> userOptional = userRepository.findByEmail(username);

            if (!userOptional.isPresent()) {
                throw new Exception("Unknown user in token");
            }
            AppUser user = userOptional.get();

            String accessToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                    .withClaim("roles", user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);

            String refreshToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                    .sign(algorithm);

            refreshTokenDTO.setAccessToken(accessToken);
            refreshTokenDTO.setRefreshToken(refreshToken);

        } catch (Exception e) {
            throw e;
        }
        return refreshTokenDTO;
    }
}
