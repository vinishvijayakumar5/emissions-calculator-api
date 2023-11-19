package com.xyzcorp.api.emissionscalculator.security;

import com.xyzcorp.api.emissionscalculator.dto.UserDetailsDto;
import com.xyzcorp.api.emissionscalculator.entity.User;
import com.xyzcorp.api.emissionscalculator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static java.util.Objects.nonNull;

@Service
public class UserDetailsServiceProvider implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAddress(username);
        if(nonNull(user )) {
            return new UserDetailsDto(username, user.getPassword(), Arrays.asList(user.getRole().getName()));
        }
        throw new UsernameNotFoundException("User not found");
    }
}
