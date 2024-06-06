package com.gildas.springBaseProjet.repository;

import com.gildas.springBaseProjet.entity.JwtEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface JwtRepository extends CrudRepository<JwtEntity, Long> {

    Optional<JwtEntity> findByValeurAndDesactiveAndExpire(String valeur, boolean desactive, boolean expire);

    Optional<JwtEntity> findByValeur(String value);

    @Query("FROM JwtEntity j WHERE j.expire = :expire AND j.desactive = :desactive AND j.users.email = :email")
    Optional<JwtEntity> findUtilisateurValidToken(String email, boolean desactive, boolean expire);



    @Query("FROM JwtEntity j WHERE j.users.email=:email")
    Stream<JwtEntity> findUtilisateur(String email);


    void deleteAllByExpireAndDesactive(boolean expire, boolean desactive);
}
