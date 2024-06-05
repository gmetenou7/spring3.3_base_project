package com.gildas.springBaseProjet.controller;

import com.gildas.springBaseProjet.config.JwtService;
import com.gildas.springBaseProjet.dto.AuthentificationDTO;
import com.gildas.springBaseProjet.entity.UsersEntity;
import com.gildas.springBaseProjet.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "users", consumes = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class usersController {

   private final UserService userService;
   private AuthenticationManager authenticationManager;
   private JwtService jwtService;

    @PostMapping(path = "inscription")
    public void inscription(@RequestBody UsersEntity usersEntity){
        this.userService.inscription(usersEntity);
    }

    @PostMapping(path = "activation")
    public void activation(@RequestBody Map<String, String> activation){
        this.userService.activation(activation);
    }

    @PostMapping(path = "connexion")
    public Map<String, String> connexion(@RequestBody AuthentificationDTO authentificationDTO){
       final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentificationDTO.username(), authentificationDTO.password())
        );

       if(authenticate.isAuthenticated()){
           return this.jwtService.generateToken(authentificationDTO.username());
       }
        return null;
    }

}
