package com.gildas.springBaseProjet.config;


import com.gildas.springBaseProjet.entity.JwtEntity;
import com.gildas.springBaseProjet.entity.UsersEntity;
import com.gildas.springBaseProjet.repository.JwtRepository;
import com.gildas.springBaseProjet.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
@Slf4j
public class JwtService {

    public static final String BEARER = "bearer";

    @Autowired
    private final Environment environment;
    private UserService userService;
    private JwtRepository jwtRepository;


    public Map<String, String> generateToken(String username) {
        UsersEntity usersEntity  = this.userService.loadUserByUsername(username);
        final Map<String, String> jwtMap = this.generateJwt(usersEntity);

       final JwtEntity jwtEntity =
               JwtEntity
                        .builder()
                       .valeur(jwtMap.get(BEARER))
                        .desactive(false)
                        .expire(false)
                       .expire(false)
                       .users(usersEntity)
                       .build();

        this.jwtRepository.save(jwtEntity);
        return jwtMap;
    }

    public String readUserNameOnDB(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public Boolean isTokenExpired(String token) {
        Date expirationDate = this.getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return
                Jwts.parser()
                        .verifyWith(this.getKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
    }

    private Map<String, String> generateJwt(UsersEntity usersEntity) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 1000 * 60 * 60 * 24;

        Map<String, Object> claims = Map.of(
                "username", usersEntity.getUsername(),
                "email", usersEntity.getEmail(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, usersEntity.getEmail()
        );

        String bearer = Jwts.builder()
                .issuedAt(new Date(currentTime))
                .expiration(new Date(expirationTime))
                .subject(usersEntity.getUsername())
                .claims(claims)
                .signWith(getKey())
                .compact();
        return Map.of(BEARER, bearer);
    }

    private SecretKey getKey() {
        String ENCRYPTION_KEY = environment.getProperty("jwt.secretKey");
        final byte[] decoder =  Decoders.BASE64.decode(ENCRYPTION_KEY);
        if (decoder == null) {
            log.error("Tentative de décodage avec une valeur nulle détectée");
            throw new IllegalStateException("Tentative de décodage avec une valeur nulle détectée");
        }
        return Keys.hmacShaKeyFor(decoder);
    }


}
