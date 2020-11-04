package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.dto.AuthenticationResponse;
import com.gomito.Gomitobackend.dto.LoginRequest;
import com.gomito.Gomitobackend.dto.SignUpRequest;
import com.gomito.Gomitobackend.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@CrossOrigin("*")
public class SignUpController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody SignUpRequest signUpRequest){
        System.out.println(signUpRequest);
    authService.signUp(signUpRequest);
    return new ResponseEntity<>("Bạn đăng ký thành công", HttpStatus.OK);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Tài khoản được kích hoạt thành công", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

}
