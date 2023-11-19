package com.xyzcorp.api.emissionscalculator.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:UNDEFINED}")
    private String secretKey;

    public String createToken(String name, String emailAddress) {

        Claims claims = Jwts.claims()
                .add("name", name)
                .build();

        return Jwts.builder()
                .subject(emailAddress)
                .claims(claims)
                .issuer("emissions-calculator-api")
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 86400000))
                .signWith(getSignKey())
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
