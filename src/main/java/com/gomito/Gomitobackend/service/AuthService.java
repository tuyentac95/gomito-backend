package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.Exception.SpringGomitoException;
import com.gomito.Gomitobackend.dto.*;
import com.gomito.Gomitobackend.model.GUser;

import com.gomito.Gomitobackend.model.MailRequest;
import com.gomito.Gomitobackend.model.VerificationToken;
import com.gomito.Gomitobackend.repository.GUserRepository;
import com.gomito.Gomitobackend.repository.VerificationTokenRepository;
import com.gomito.Gomitobackend.security.JwtProvider;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {
    private final GUserRepository gUserRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    public static final String FROM_EMAIL = "langquang1995@gmail.com";

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        GUser gUser = new GUser();
        gUser.setUsername(signUpRequest.getUsername());
        gUser.setEmail(signUpRequest.getEmail());
        gUser.setPassword(encodePassword(signUpRequest.getPassword()));
        gUser.setEnabled(false);
//        gUser.setEnabled(true);
        gUserRepository.save(gUser);

        String token = generateVerificationToken(gUser);

        // gửi mail
        MailRequest mailRequest = new MailRequest();
        mailRequest.setName(signUpRequest.getUsername());
        mailRequest.setTo(signUpRequest.getEmail());
        mailRequest.setSubject("Chúc mừng bạn " + signUpRequest.getUsername() + " đã đăng ký thành công!");
        mailRequest.setFrom(FROM_EMAIL);

        Map<String, Object> model = new HashMap<>();
        model.put("Username", signUpRequest.getUsername());
        model.put("Email", signUpRequest.getEmail());
        model.put("message", "http://localhost:4200/login?isRegistered=true&verifyToken=" + token);

        mailService.sendMail(mailRequest, model, "email-template-signup.ftl");

//        mailService.setMail(new NotificationEmail("Please Activate your Account",
//                gUser.getEmail(),"Thank you for signing up to GOMITO, " +
//                "please click on the below url to activate your account:\n" +
//                "http://localhost:8080/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(GUser gUser) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(gUser);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public AuthenticationResponse login(LoginRequest loginRequest){
        GUser user = gUserRepository.findByUsername(loginRequest.getUsername())
                .orElse(null);

        if (user != null) {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                    loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(authentication);

            return AuthenticationResponse.builder()
                    .authenticationToken(token)
                    .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                    .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                    .username(loginRequest.getUsername())
                    .userId(user.getUserId())
                    .status(200)
                    .message("Login successful")
                    .build();
        } else {
            return AuthenticationResponse.builder()
                    .status(404)
                    .message("username not found")
                    .build();
        }
    }

    public void verifyAccount(String token){
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new SpringGomitoException("Mã không hợp lệ"));
        fetchUserAndEnable(verificationTokenOptional.get());
    }


     private void fetchUserAndEnable(VerificationToken verificationToken){
        String userName = verificationToken.getUser().getUsername();
        GUser gUser = gUserRepository.findByUsername(userName).orElseThrow(() -> new SpringGomitoException("Không tìm thấy người dùng với id - " + userName));
        gUser.setEnabled(true);
        gUserRepository.save(gUser);
    }


    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());

        GUser user = gUserRepository.findByUsername(refreshTokenRequest.getUsername())
                .orElseThrow(() -> new SpringGomitoException("User not found with name " + refreshTokenRequest.getUsername()));

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .userId(user.getUserId())
                .build();
    }

    public GUser getCurrentUser() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return gUserRepository.findByUsername(principal.getUsername()).orElse(null);
    }

    public boolean userNotExist(SignUpRequest signUpRequest) {
        GUser user = gUserRepository.findByUsername(signUpRequest.getUsername())
                .orElse(null);

        if (user == null) {
            GUser gUser = gUserRepository.findByEmail(signUpRequest.getEmail())
                    .orElse(null);
            return (gUser == null);
        }
        return false;
    }

    public boolean changePassword(ChangePasswordRequest request) {
        GUser user = getCurrentUser();
        LoginRequest loginRequest = new LoginRequest(user.getUsername(), request.getOldPassword());
        AuthenticationResponse auth = login(loginRequest);
        if (auth.getStatus() == 200) {
            user.setPassword(encodePassword(request.getNewPassword()));
            gUserRepository.save(user);
            return true;
        }       
        return false;
    }
}
