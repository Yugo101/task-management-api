package com.example.api_practice.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(!username.equals("test")){
            throw new UsernameNotFoundException("User not found");
        }

        return new User(
                "test",
                "{noop}password",
                Collections.emptyList()
        );
    }
}
