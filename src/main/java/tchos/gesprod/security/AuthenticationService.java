package tchos.gesprod.security;
// 10

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tchos.gesprod.exception.AccountNotActivatedException;
import tchos.gesprod.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UtilisateurRepository repository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CacheManager cacheManager;
    private final AuthenticationManager authenticationManager;
    private final VerificationTokenRepository verificationTokenRepository;

    @Value("${app.token.expiration.minutes}")
    private long tokenExpirationMinutes;

    public AuthenticationResponse register(RegisterRequest registerRequest) throws MessagingException {
        // 1. Création de l'utilisateur (sans l'activer)
        // créer user désactivé
        var user = Utilisateur.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .enabled(false) // important
                .build();

        repository.save(user);

        // créer token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(tokenExpirationMinutes)) // inject via @Value
                .used(false)
                .build();
        verificationTokenRepository.save(verificationToken);

        // envoyer email afin que le user confirme l'activation de son compte
        emailService.sendVerificationEmail(user, token);

        // Ne pas renvoyer de JWT avant activation ; retourner message d'attente
        return AuthenticationResponse.builder()
                .token(null) // ou message non-null si tu ajustes AuthResponse
                .build();
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // 1. Vérifier si l'utilisateur existe
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Cet utilisateur n'existe pas !!!"));

        // 2. Vérifier si le compte est activé
        if (!user.isEnabled()) {
            throw new AccountNotActivatedException(
                    "Votre compte n'est pas encore activé. Veuillez vérifier votre e-mail."
            );
        }

        // 3. Authentification Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 4. Générer le token JWT
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public String confirmToken(String token) {
        VerificationToken vt = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token invalide"));

        if (vt.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token déjà utilisé");
        }
        if (vt.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expiré");
        }

        Utilisateur user = vt.getUser();
        user.setEnabled(true);
        repository.save(user);

        vt.setUsed(true);
        verificationTokenRepository.save(vt);

        // Option: supprimer le token après usage
        // verificationTokenRepository.delete(vt);
        return "Félicitations " + user.getFirstname() + " !!! Votre compte sur GesProd a été activé avec succès !!!";
    }
}
