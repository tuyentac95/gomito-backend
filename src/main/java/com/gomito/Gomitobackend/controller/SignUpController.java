package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.dto.SignUpRequest;
import com.gomito.Gomitobackend.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class SignUpController {
    private final AuthService authService;

    @PostMapping("/sigup")
    public ResponseEntity signUp(@RequestBody SignUpRequest signUpRequest){
        authService.
    }
}
