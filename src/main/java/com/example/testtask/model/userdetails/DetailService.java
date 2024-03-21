package com.example.testtask.model.userdetails;

import com.example.testtask.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "detailService")
public class DetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public DetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
        return this
                .userRepository
                .findByPrincipal(principal)
                .map(CustomUserDetails::new) //
                .orElseThrow(() -> new UsernameNotFoundException(principal + " not found"));
    }

}