package com.gildas.springBaseProjet.repository;

import com.gildas.springBaseProjet.entity.ValidationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationRepository extends CrudRepository<ValidationEntity, Long> {
    Optional<ValidationEntity> findByCode(String code);
}
