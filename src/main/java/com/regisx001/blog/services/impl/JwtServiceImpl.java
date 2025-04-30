package com.regisx001.blog.services.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.regisx001.blog.services.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

/**
 * Implementation of the JwtService interface.
 * This service provides methods for generating, validating, and extracting
 * information from JWT tokens.
 */
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token the JWT token
     * @return the username (subject) contained in the token
     */
    @Override
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Extracts specific claims from the JWT token using a claims resolver function.
     *
     * @param token          the JWT token
     * @param claimsResolver a function to resolve specific claims
     * @param <T>            the type of the resolved claim
     * @return the resolved claim
     */
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token
     * @return the claims contained in the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails the user details
     * @return the generated JWT token
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token with additional claims for the given user details.
     *
     * @param extraClaims additional claims to include in the token
     * @param userDetails the user details
     * @return the generated JWT token
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Builds a JWT token with the specified claims, user details, and expiration
     * time.
     *
     * @param extraClaims   additional claims to include in the token
     * @param userDetails   the user details
     * @param jwtExpiration the expiration time in milliseconds
     * @return the built JWT token
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long jwtExpiration) {
        // DEPRECATED
        // return Jwts.builder()
        // .setClaims(extraClaims)
        // .setSubject(userDetails.getUsername())
        // .setIssuedAt(new Date(System.currentTimeMillis()))
        // .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        // .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Fixed to HS256
        // .compact();

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey()) // Fixed to HS256
                .compact();
    }

    /**
     * Retrieves the signing key used for signing the JWT token.
     *
     * @return the signing key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Validates the JWT token for the given user details.
     *
     * @param token       the JWT token
     * @param userDetails the user details
     * @return true if the token is valid, false otherwise
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    private Date extractExpiration(String token) { // Fixed method name
        return extractClaims(token, Claims::getExpiration);
    }

    @Override
    public long getJwtExpiration() {
        return this.jwtExpiration;
    }
}
