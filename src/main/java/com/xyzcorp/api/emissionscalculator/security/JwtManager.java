package com.xyzcorp.api.emissionscalculator.security;

import com.xyzcorp.api.emissionscalculator.exception.AuthTokenNotFoundException;
import com.xyzcorp.api.emissionscalculator.exception.UnAuthorisedAccessException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.lang.String.format;

@Component
public final class JwtManager extends JwtTokenProvider {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Autowired
    private UserDetailsService userDetailsService;

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(7);
        }
        throw new AuthTokenNotFoundException("Authorisation token not found", "AUTH001", HttpStatus.FORBIDDEN);
    }

    public String getEmail(HttpServletRequest httpServletRequest) {
        final String token = resolveToken(httpServletRequest);
        final Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    public void validateToken(HttpServletRequest httpServletRequest) {
        final String token = resolveToken(httpServletRequest);
        final Claims claims = extractClaims(token);

        if(ifTokenNotExpiredVerifyIssuer(claims)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            throw new UnAuthorisedAccessException(format("Unauthorised access for email [%s]", claims.getSubject()),
                    "AUTH002", HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean ifTokenNotExpiredVerifyIssuer(Claims claims) {
        return claims.getExpiration().after(new Date()) && ISSUER.equalsIgnoreCase(claims.getIssuer());
    }
}
