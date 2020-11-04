package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.repository.GUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static java.util.Collections.singletonList;
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final GUserRepository gUserRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<GUser> userOptional = gUserRepository.findByUsername(username);
        GUser gUser = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user" + "Found with username : " + username));
        return new org.springframework.security
                .core.userdetails.User(gUser.getUsername(), gUser.getPassword(),
                gUser.isEnabled(), true,true,
                true, getAuthorities("USER"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return singletonList(new SimpleGrantedAuthority(role));
    }
}
