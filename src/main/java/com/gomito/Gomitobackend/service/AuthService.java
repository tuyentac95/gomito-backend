package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.Exception.SpringGomitoException;
import com.gomito.Gomitobackend.dto.AuthenticationResponse;
import com.gomito.Gomitobackend.dto.LoginRequest;
import com.gomito.Gomitobackend.dto.SignUpRequest;
import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.model.NotificationEmail;
import com.gomito.Gomitobackend.model.VerificationToken;
import com.gomito.Gomitobackend.repository.GUserRepository;
import com.gomito.Gomitobackend.repository.VerificationTokenRepository;
import com.gomito.Gomitobackend.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final GUserRepository gUserRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        GUser gUser = new GUser();
        gUser.setUsername(signUpRequest.getUsername());
        gUser.setEmail(signUpRequest.getEmail());
        gUser.setPassword(encodePassword(signUpRequest.getPassword()));
        gUser.setEnabled(false);
        gUserRepository.save(gUser);

        String token = generateVerificationToken(gUser);
        mailService.setMail(new NotificationEmail("Please Activate your Account",
                gUser.getEmail(),"Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/auth/accountVerification/" + token));
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
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        String authenticationToken = jwtProvider.generateToken(authenticate);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return new AuthenticationResponse(authenticationToken, loginRequest.getUsername());
    }

    public void verifyAccount(String token){
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new SpringGomitoException("Mã không hợp lệ"));
        fetchUserAndEnable(verificationTokenOptional.get());
    }

     @Transactional
    void fetchUserAndEnable(VerificationToken verificationToken){
        String userName = verificationToken.getUser().getUsername();
        GUser gUser = gUserRepository.findByUsername(userName).orElseThrow(() -> new SpringGomitoException("Không tìm thấy người dùng với id - " + userName));
        gUser.setEnabled(true);
        gUserRepository.save(gUser);
    }
}
