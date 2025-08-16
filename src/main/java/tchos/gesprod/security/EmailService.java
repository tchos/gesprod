package tchos.gesprod.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String appBaseUrl;

    public void sendVerificationEmail(Utilisateur user, String token) {
        String link = appBaseUrl + "/api/auth/confirm?token=" + token;
        String subject = "Activation de votre compte";
        String text = "Bonjour " + user.getFirstname() + ",\n\n" +
                "Merci de vous être inscrit sur GesProd. Veuillez activer votre compte en cliquant sur le lien suivant :\n" +
                link + "\n\n" +
                "Ce lien expirera dans 24 heures.\n\n" +
                "Si vous n'avez pas créé de compte, ignorez cet e-mail.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    // Option: méthode pour envoyer un code au lieu d'un lien
    public void sendVerificationCode(Utilisateur user, String code) {
        String subject = "Code d'activation";
        String text = "Bonjour " + user.getFirstname() + ",\n\n" +
                "Voici votre code d'activation : " + code + "\n\n" +
                "Ce code expirera dans 24 heures.";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

