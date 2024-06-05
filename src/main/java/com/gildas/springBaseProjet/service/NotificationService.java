package com.gildas.springBaseProjet.service;

import com.gildas.springBaseProjet.assets.errors.specific.EmailSendingException;
import com.gildas.springBaseProjet.entity.ValidationEntity;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {

    private JavaMailSender mailSender;
    public void envoyer(ValidationEntity validationEntity) {
        try {


            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("gildas@gmail.com");
            message.setTo(validationEntity.getUser().getEmail());
            message.setSubject("Votre code d'activation");

            String text = String.format(
                    "Bonjour %s <br /> votre code d'activation est %s; A bientôt",
                    validationEntity.getUser().getUsername(),
                    validationEntity.getCode()
            );
            message.setText(text);
            mailSender.send(message);
        }catch (MailException e){
            throw new EmailSendingException("Erreur lors de l'envoi de l'email à " + validationEntity.getUser().getEmail(), e);
        }
    }
}
