package com.gildas.springBaseProjet.controller;

import com.gildas.springBaseProjet.entity.AvisEntity;
import com.gildas.springBaseProjet.service.AvisService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "avis", consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@SecurityRequirement(name="Bearer Authentication")
public class AvisController {

    private final AvisService avisService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void creer(@RequestBody AvisEntity avis) {
      this.avisService.creer(avis);
    }

}
