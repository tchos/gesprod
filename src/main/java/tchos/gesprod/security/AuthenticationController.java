package tchos.gesprod.security;
//13

import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tchos.gesprod.exception.InvalidVerificationCodeException;
import tchos.gesprod.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "API pour l'authentification")
public class AuthenticationController {

    private final UtilisateurRepository utilisateurRepository;
    private final AuthenticationService service;
    private final LogoutService logoutService;
    private final ConcurrentMapCacheManager cacheManager;
    private final JwtService jwtService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    @Value("${app.token.expiration.minutes}")
    private long tokenExpirationMinutes;

    @PostMapping("/register")
    @Operation(
            summary = "Enregistrer un nouvel utilisateur",
            description = "Cette méthode vous permet de créer le compte d'un nouvel utilisateur",
            operationId = "register",
            tags = {"Authentification"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Compte créé avec succès")
            }
    )
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) throws MessagingException {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    @Operation(
            summary = "Authentifier un utilisateur",
            description = "Cette méthode permet à un utilisateur de se connecter à l'API",
            operationId = "login",
            tags = {"Authentification"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Connexion effectuée avec succès")
            }
    )
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Se déconnecter de l'API",
            description = "Cette méthode permet à un utilisateur de se déconnecter à l'API",
            operationId = "logout",
            tags = {"Authentification"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Déconnexion effectuée avec succès")
            }
    )
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
        return ResponseEntity.ok("Déconnexion réussie !");
    }

    @Operation(
            summary = "Pour confirmer l'activation du compte",
            description = "Cette méthode permet à un utilisateur de se rendre " +
                    "dans sa boite email pour cliquer sur le lien de confirmation d'activation de son compte",
            operationId = "confirm",
            tags = {"Authentification"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Confirmation d'activation du compte envoyé par email")
            }
    )
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        return ResponseEntity.ok(service.confirmToken(token));
    }

    @Operation(
            summary = "Pour renvoyer le lien d'activation du compte",
            description = "Cette méthode permet de renvoyer le lien d'activation à un utilisateur dans sa boite email ",
            operationId = "resendActivation",
            tags = {"Authentification"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Confirmation d'activation du compte envoyé par email")
            }
    )
    @PostMapping("/resend-activation")
    public ResponseEntity<String> resendActivation(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        if (user.isEnabled()) {
            return ResponseEntity.badRequest().body("Le compte est déjà activé");
        }

        // supprimer anciens tokens
        verificationTokenRepository.deleteAllByUser(user);

        String token = UUID.randomUUID().toString();
        VerificationToken vt = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(tokenExpirationMinutes))
                .used(false)
                .build();
        verificationTokenRepository.save(vt);
        emailService.sendVerificationEmail(user, token);

        return ResponseEntity.ok("E-mail d'activation renvoyé");
    }

}
