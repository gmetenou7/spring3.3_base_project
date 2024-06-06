package com.gildas.springBaseProjet.controller;

import com.gildas.springBaseProjet.entity.AvisEntity;
import com.gildas.springBaseProjet.service.AvisService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



@SecurityRequirement(name="Bearer Authentication")
@Tag(name = "Gestion Avis API")
@RequestMapping("/avis")
@AllArgsConstructor
@RestController
public class AvisController {

    private final AvisService avisService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void creer(@RequestBody AvisEntity avis) {
      this.avisService.creer(avis);
    }

}
