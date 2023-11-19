package com.xyzcorp.api.emissionscalculator.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
public class UserDetailsDto implements UserDetails {

     private String emailAddress;
     private String pass;
     private List<String> roles;

    public UserDetailsDto(String emailAddress, String pass, List<String> roles) {
        this.emailAddress = emailAddress;
        this.pass = pass;
        this.roles = roles;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(nonNull(roles)) {
            return roles.stream()
                    .map(role-> new SimpleGrantedAuthority("ROLE_"+role))
                    .collect(Collectors.toUnmodifiableList());
        }
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public String getUsername() {
        return emailAddress;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
