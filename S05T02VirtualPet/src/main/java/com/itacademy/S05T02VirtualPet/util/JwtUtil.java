package com.itacademy.S05T02VirtualPet.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    public Mono<String> extractUsername(String token) {
        return extractClaim(token, Claims::getSubject)
                .doOnNext(username -> log.info("Extracted username from token: {}", username))
                .doOnError(error -> log.error("Error extracting username from token: {}", token, error));
    }

    public <T> Mono<T> extractClaim(String token, Function<Claims, T> claimsResolver) {
        return Mono.fromCallable(() -> {
                    Claims claims = extractAllClaims(token);
                    return claimsResolver.apply(claims);
                }).doOnNext(claim -> log.info("Extracted claim: {}", claim))
                .doOnError(error -> log.error("Error extracting claim from token: {}", token, error));
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
                .compact();
    }

    public Mono<Boolean> isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token)
                .doOnNext(username -> log.info("Validating token for username: {}", username))
                .flatMap(username -> {
                    boolean isValid = username.equals(userDetails.getUsername());
                    log.info("Token valid for user {}: {}", userDetails.getUsername(), isValid);
                    return Mono.just(isValid);
                })
                .doOnError(error -> log.error("Error validating token for user: {}", userDetails.getUsername(), error));
    }

    private Mono<Boolean> isTokenExpired(String token) {
        return extractExpiration(token)
                .doOnNext(expirationDate -> log.info("Token expiration date: {}", expirationDate))
                .map(expirationDate -> expirationDate.before(new Date()))
                .doOnError(error -> log.error("Error checking token expiration: {}", token, error));
    }

    private Mono<Date> extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration)
                .doOnNext(expiration -> log.info("Extracted expiration date from token: {}", expiration))
                .doOnError(error -> log.error("Error extracting expiration date from token: {}", token, error));
    }

    public Claims extractAllClaims(String token) {
        log.debug("Extracting all claims from token");
        return Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(token).getBody();
    }


    private SecretKey getSignInKey() {
        log.debug("Getting the signing key");
        try {
            byte[] keyBytes = Base64.getDecoder().decode(secretKey);
            return new SecretKeySpec(keyBytes, "HmacSHA256");
        } catch (IllegalArgumentException e) {
            byte[] keyBytes = new BigInteger(secretKey, 16).toByteArray();
            return new SecretKeySpec(keyBytes, "HmacSHA256");
        }
    }
}

