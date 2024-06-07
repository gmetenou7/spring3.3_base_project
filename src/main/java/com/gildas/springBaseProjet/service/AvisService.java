package com.gildas.springBaseProjet.service;

import com.gildas.springBaseProjet.entity.AvisEntity;
import com.gildas.springBaseProjet.entity.UsersEntity;
import com.gildas.springBaseProjet.repository.AvisRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AvisService {

    private final AvisRepository avisRepository;

    public void creer(AvisEntity avis) {
        UsersEntity users = (UsersEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        avis.setUsers(users);
        this.avisRepository.save(avis);
    }


    public List<AvisEntity> liste_avis() {
        return (List<AvisEntity>) this.avisRepository.findAll();
    }
}
