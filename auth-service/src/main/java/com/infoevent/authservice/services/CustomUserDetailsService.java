package com.infoevent.authservice.services;

import com.infoevent.authservice.clients.UserRestClient;
import com.infoevent.authservice.config.CustomUserDetails;
import com.infoevent.authservice.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private UserRestClient userRestClient;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User credential = userRestClient.getUserByEmail(email);
        return Optional.ofNullable(credential)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found with email: " + email));
    }
}
