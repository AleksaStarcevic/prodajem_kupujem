package com.example.prodajem_kupujem.controllers;

import com.example.prodajem_kupujem.dto.users.UserRegistrationDTO;
import com.example.prodajem_kupujem.exceptions.InvalidValidationTokenException;
import com.example.prodajem_kupujem.exceptions.UserNotFoundException;
import com.example.prodajem_kupujem.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @GetMapping("/refreshToken")
    public ResponseEntity<?> sendRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
          return new ResponseEntity<>(authService.sendRefreshToken(request, response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) throws UserNotFoundException {
        authService.register(userRegistrationDTO);
        return new ResponseEntity<>("Check your email to activate your account", HttpStatus.OK);
    }

    @GetMapping("/registerConfirmation/{token}")
    public ResponseEntity<?> registerConfirmation(@PathVariable String token) throws InvalidValidationTokenException {
        authService.registerConfirmation(token);
        return new ResponseEntity<>("Account successfully activated!", HttpStatus.OK);
    }
}
