package com.gildas.springBaseProjet.repository;

import com.gildas.springBaseProjet.entity.AvisEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvisRepository extends CrudRepository<AvisEntity, Long> {

}
