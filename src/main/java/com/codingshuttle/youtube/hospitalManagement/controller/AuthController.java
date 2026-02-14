package com.codingshuttle.youtube.hospitalManagement.controller;

import com.codingshuttle.youtube.hospitalManagement.dto.reponse.LoginResponseDto;
import com.codingshuttle.youtube.hospitalManagement.dto.reponse.RegisterResponseDto;
import com.codingshuttle.youtube.hospitalManagement.dto.request.LoginRequestDto;
import com.codingshuttle.youtube.hospitalManagement.dto.request.RegisterRequestDto;
import com.codingshuttle.youtube.hospitalManagement.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto response = authService.login(loginRequestDto);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) throws IllegalAccessException {
        return ResponseEntity.ok(authService.register(registerRequestDto));
    }
}