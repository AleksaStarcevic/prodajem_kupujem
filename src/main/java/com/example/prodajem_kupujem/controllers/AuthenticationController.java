package com.example.prodajem_kupujem.controllers;

import com.example.prodajem_kupujem.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
}
