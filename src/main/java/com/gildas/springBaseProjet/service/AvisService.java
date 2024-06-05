package com.gildas.springBaseProjet.service;

import com.gildas.springBaseProjet.entity.AvisEntity;
import com.gildas.springBaseProjet.repository.AvisRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AvisService {

    private final AvisRepository avisRepository;

    public void creer(AvisEntity avis) {
        this.avisRepository.save(avis);
    }

}
