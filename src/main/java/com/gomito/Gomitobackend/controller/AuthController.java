package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.dto.AuthenticationResponse;
import com.gomito.Gomitobackend.dto.LoginRequest;
import com.gomito.Gomitobackend.dto.RefreshTokenRequest;
import com.gomito.Gomitobackend.dto.SignUpRequest;
import com.gomito.Gomitobackend.service.AuthService;
import com.gomito.Gomitobackend.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        if (authService.userNotExist(signUpRequest)) {
            authService.signUp(signUpRequest);
            return new ResponseEntity<>("Bạn đăng ký thành công", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Người dùng đã tồn tại");
        }
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Tài khoản được kích hoạt thành công", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        System.out.println(refreshTokenRequest);
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }
}
