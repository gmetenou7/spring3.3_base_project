package com.gildas.springBaseProjet.service;

import com.gildas.springBaseProjet.assets.TypeRoles;
import com.gildas.springBaseProjet.assets.errors.specific.ResourceNotFoundException;
import com.gildas.springBaseProjet.entity.RoleEntity;
import com.gildas.springBaseProjet.entity.UsersEntity;
import com.gildas.springBaseProjet.entity.ValidationEntity;
import com.gildas.springBaseProjet.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ValidationService validationService;

    public void inscription(UsersEntity user) {

        Optional<UsersEntity> usersEntity =  this.userRepository.findByEmail(user.getEmail());
        if(usersEntity.isPresent()) {
            throw new ResourceNotFoundException("cet email est déjà utilisé");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        RoleEntity role = new RoleEntity();
        role.setLibelle(TypeRoles.USERS);
        user.setRole(role);

        user = this.userRepository.save(user);
        this.validationService.enregistrer(user);
    }

    public void activation(Map<String, String> activation) {
        ValidationEntity validation = this.validationService.rechercheEnFonctionDuCode(activation.get("code"));
        if(Instant.now().isAfter(validation.getExpiration())){
            throw new ResourceNotFoundException("votre code à expiré");
        }

       UsersEntity users = this.userRepository.findById(validation.getUser().getId()).orElseThrow(
               () -> new ResourceNotFoundException("l'utilisateur n'existe pas")
        );

        users.setActif(true);
        this.userRepository.save(users);
    }

    @Override
    public UsersEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username)
                .orElseThrow(
                    () -> new UsernameNotFoundException("L'utilisateur n'existe pas")
                );
    }

    public void userPasswordUpdate(Map<String, String> body) {
        UsersEntity usersEntity = this.loadUserByUsername(body.get("email"));
        this.validationService.enregistrer(usersEntity);
    }

    public void newPasswordUpdate(Map<String, String> body) {
        UsersEntity usersEntity = this.loadUserByUsername(body.get("email"));
        final ValidationEntity validation = validationService.rechercheEnFonctionDuCode(body.get("code"));
        if(validation.getUser().getEmail().equals(usersEntity.getEmail())) {
            String mdpCrypte = this.passwordEncoder.encode(body.get("password"));
            usersEntity.setPassword(mdpCrypte);
            this.userRepository.save(usersEntity);
        }
    }



}
