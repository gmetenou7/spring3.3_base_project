package com.gildas.springBaseProjet.repository;

import com.gildas.springBaseProjet.entity.UsersEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UsersEntity, Long> {
    Optional<UsersEntity> findByEmail(String email);
}
