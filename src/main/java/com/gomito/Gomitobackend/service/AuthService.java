package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.dto.SignUpRequest;
import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.repository.GUserRepository;
import com.gomito.Gomitobackend.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final GUserRepository gUserRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        GUser gUser = new GUser();
        gUser.setUsername(signUpRequest.getUsername());
        gUser.setEmail(signUpRequest.getEmail());
        gUser.setPassword(encodePassword(signUpRequest.getPassword()));
        gUser.setEnabled(false);
        gUserRepository.save(gUser);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
