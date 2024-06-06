package com.gildas.springBaseProjet.config;


import com.gildas.springBaseProjet.assets.errors.specific.ResourceNotFoundException;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
@Slf4j
public class JwtService {

    public static final String BEARER = "bearer";

    @Autowired
    private final Environment environment;
    private UserService userService;
    private JwtRepository jwtRepository;

    public JwtEntity tokenByValue(String value) {
        return this.jwtRepository.findByValeurAndDesactiveAndExpire(
                value,
                false,
                false
        ).orElseThrow(() -> new ResourceNotFoundException("Token invalide ou inconnu"));
    }

    public Map<String, String> generateToken(String username) {
        UsersEntity usersEntity  = this.userService.loadUserByUsername(username);
        this.disableTokens(usersEntity);
        final Map<String, String> jwtMap = new HashMap<>(this.generateJwt(usersEntity));

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

    private void disableTokens(UsersEntity usersEntity) {

        final List<JwtEntity> jwtEntities = this.jwtRepository.findUtilisateur(usersEntity.getEmail()).peek(
                jwtEntity -> {
                    jwtEntity.setExpire(true);
                    jwtEntity.setDesactive(true);
                }
        ).collect(Collectors.toList());
        this.jwtRepository.saveAll(jwtEntities);
    }

    public String readUserNameOnDB(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public Boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return this.getClaim(token, Claims::getExpiration);
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

        final Map<String, Object> claims = Map.of(
                "username", usersEntity.getUsername(),
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
        return Keys.hmacShaKeyFor(decoder);
    }

    public void deconnexion() {
        UsersEntity usersEntity = (UsersEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        JwtEntity jwtEntity = this.jwtRepository.findUtilisateurValidToken(
                usersEntity.getEmail(),
                false,
                false
        ).orElseThrow(() -> new RuntimeException("Token invalide"));
        jwtEntity.setExpire(true);
        jwtEntity.setDesactive(true);
        this.jwtRepository.save(jwtEntity);
    }

    @Scheduled(cron = "0 * * * * *") //toutes les minutes
    public void removeUselessJwt() {
        log.info("Suppression des token à {}", Instant.now());
        this.jwtRepository.deleteAllByExpireAndDesactive(true, true);
    }

}
