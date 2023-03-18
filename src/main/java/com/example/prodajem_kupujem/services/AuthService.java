package com.example.prodajem_kupujem.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.prodajem_kupujem.dto.tokens.RefreshTokenDTO;
import com.example.prodajem_kupujem.dto.users.UserRegistrationDTO;
import com.example.prodajem_kupujem.email.Email;
import com.example.prodajem_kupujem.email.EmailService;
import com.example.prodajem_kupujem.entities.AppUser;
import com.example.prodajem_kupujem.entities.UserRole;
import com.example.prodajem_kupujem.entities.ValidationToken;
import com.example.prodajem_kupujem.exceptions.InvalidValidationTokenException;
import com.example.prodajem_kupujem.exceptions.UserNotFoundException;
import com.example.prodajem_kupujem.repositories.UserRepository;
import com.example.prodajem_kupujem.repositories.UserRoleRepository;
import com.example.prodajem_kupujem.repositories.ValidationTokenRepository;
import com.example.prodajem_kupujem.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.prodajem_kupujem.config.Constants.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationTokenRepository validationTokenRepository;
    private final UserRoleRepository userRoleRepository;

    private final EmailService emailService;


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
            AppUser appUser = userOptional.get();
            UserDetailsImpl user = new UserDetailsImpl(appUser);

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

    public void register(UserRegistrationDTO userRegistrationDTO) throws UserNotFoundException {
        Optional<AppUser> userOptional = userRepository.findByEmail(userRegistrationDTO.getEmail());
        if(userOptional.isPresent()) throw new UserNotFoundException("User is already registered, go to login page");

        Optional<UserRole> roleOptional = userRoleRepository.findByRoleNameContaining("user");

        AppUser appUser = AppUser.builder()
                .email(userRegistrationDTO.getEmail())
                .password(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .name(userRegistrationDTO.getName())
                .city(userRegistrationDTO.getCity())
                .phone(userRegistrationDTO.getPhone())
                .role(roleOptional.get())
                .enabled(false)
                .build();
        userRepository.save(appUser);
        createTokenAndSendEmail(userRegistrationDTO.getName(), userRegistrationDTO.getEmail(), appUser);
    }

    private void createTokenAndSendEmail(String name,String userEmail, AppUser appUser) {
        String randomToken = UUID.randomUUID().toString();
        ValidationToken validationToken = ValidationToken.builder()
                .token(randomToken)
                .generationTime(new Date())
                .user(appUser)
                .build();

        validationTokenRepository.save(validationToken);

        Map<String,Object> variables = Map.of("name", name,"token",randomToken);
        Email email = Email.builder()
                .from(EMAIL_SENDER)
                .to(userEmail)
                .subject("Email confirmation")
                .template(EMAIL_CONFIRMATION_TEMPLATE)
                .variables(variables)
                .build();

        emailService.sendEmail(email);
    }

    public void registerConfirmation(String token) throws InvalidValidationTokenException {
        Optional<ValidationToken> validationTokenOptional = validationTokenRepository.findByToken(token);
        if(validationTokenOptional.isEmpty()) throw new InvalidValidationTokenException("Invalid validation token");

        ValidationToken validationToken = validationTokenOptional.get();
        Date generationTime = validationToken.getGenerationTime();
        LocalDateTime time = generationTime.toInstant()
               .atZone(ZoneId.systemDefault())
               .toLocalDateTime();

        if(time.plusMinutes(VALIDATION_TOKEN_EXPIRATION).isBefore(LocalDateTime.now())) {
            createTokenAndSendEmail(validationToken.getUser().getName(),validationToken.getUser().getEmail(),validationToken.getUser());
            validationTokenRepository.delete(validationToken);
           throw new InvalidValidationTokenException("Validation token expired, check your email again to activate your account");
       }
        AppUser user = validationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
    }


}
