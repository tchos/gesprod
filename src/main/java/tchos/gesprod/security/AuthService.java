package tchos.gesprod.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthResponse authenticate(AuthRequest request) {
        try {
            // Authentification de l'utilisateur
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Récupération de l'utilisateur dans la base de données
            User user =
                    userRepository
                            .findByEmail(request.getEmail())
                            .orElseThrow(
                                    () ->
                                            new UsernameNotFoundException(
                                                    "L'utilisateur avec l'adresse e-mail "
                                                            + request.getEmail()
                                                            + " n'a pas été trouvé. "
                                                            + "Veuillez vérifier l'adresse saisie et réessayer."));

            // Génération du token JWT
            String jwtToken = jwtService.generateToken(user);

            // Réponse réussie
            return AuthResponse.builder().token(jwtToken).build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}