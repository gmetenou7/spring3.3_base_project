package com.gildas.springBaseProjet.service;

import com.gildas.springBaseProjet.assets.errors.specific.ResourceNotFoundException;
import com.gildas.springBaseProjet.entity.UsersEntity;
import com.gildas.springBaseProjet.entity.ValidationEntity;
import com.gildas.springBaseProjet.repository.ValidationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
@Service
@AllArgsConstructor
public class ValidationService {

    private final ValidationRepository validationRepository;
    private final NotificationService notificationService;

    public void enregistrer(UsersEntity usersEntity) {
        ValidationEntity validationEntity = new ValidationEntity();
        validationEntity.setUser(usersEntity);

        Instant createdAt = Instant.now();
        validationEntity.setCreation(createdAt);

        Instant expiration =createdAt.plus(10, MINUTES);
        validationEntity.setExpiration(expiration);

        Random random = new Random();
        Integer randomInt = random.nextInt(999999);
        String code = String.format("%06d", randomInt);
        validationEntity.setCode(code);

        this.validationRepository.save(validationEntity);
        notificationService.envoyer(validationEntity);
    }

    public ValidationEntity rechercheEnFonctionDuCode(String code) {
        return  this.validationRepository.findByCode(code).orElseThrow(
                () -> new ResourceNotFoundException("le code n'a pas été trouver")
        );
    }

    @Scheduled(cron = "@daily") //toutes les jours
    public void removeUselessJwt() {
        final Instant now = Instant.now();
        log.info("Suppression des code expiré à {}", now);
        this.validationRepository.deleteAllByExpirationBefore(now);
    }

}
