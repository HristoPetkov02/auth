package com.tinqinacademy.auth.core.security;


import com.tinqinacademy.auth.persistence.models.User;
import com.tinqinacademy.auth.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Not valid email or password"));

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());


        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), List.of(authority));
    }
}
