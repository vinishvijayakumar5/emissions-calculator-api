package com.xyzcorp.api.emissionscalculator.security;

import com.xyzcorp.api.emissionscalculator.exception.ClaimsParsingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

import static java.lang.String.format;

@Service
public sealed class JwtTokenProvider permits JwtManager {

    protected static final String ISSUER = "emissions-calculator-api";

    @Value("${security.jwt.token.secret-key:UNDEFINED}")
    private String secretKey;

    public String createToken(String name, String emailAddress) {

        Claims claims = Jwts.claims()
                .add("name", name)
                .build();

        return Jwts.builder()
                .subject(emailAddress)
                .claims(claims)
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 86400000))
                .signWith(getSignKey())
                .compact();
    }

    protected Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    protected Claims extractClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(new SecretKeySpec(Decoders.BASE64.decode(secretKey), "HmacSHA256"))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new ClaimsParsingException(format("Unable to parse claims from the token. Error [%s]", e.getMessage()), "AUTH001", HttpStatus.FORBIDDEN);
        }
    }

}
