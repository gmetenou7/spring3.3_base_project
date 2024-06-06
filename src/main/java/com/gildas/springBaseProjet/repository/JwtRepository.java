package com.gildas.springBaseProjet.repository;

import com.gildas.springBaseProjet.entity.JwtEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRepository extends CrudRepository<JwtEntity, Long> {
    Optional<JwtEntity> findByValeur(String value);
}
