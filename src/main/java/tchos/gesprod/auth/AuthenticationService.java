package tchos.gesprod.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            // Authentification de l'utilisateur
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Récupération de l'utilisateur dans la base de données
            User user = userRepository
                            .findByEmail(request.getEmail())
                            .orElseThrow(() -> new UsernameNotFoundException(
                                                    "L'utilisateur avec l'adresse e-mail "
                                                            + request.getEmail()
                                                            + " n'a pas été trouvé. "
                                                            + "Veuillez vérifier l'adresse saisie et réessayer."));

            // Génération du token JWT
            String jwtToken = jwtService.generateToken(user);

            // Réponse réussie
            return AuthenticationResponse.builder().token(jwtToken).build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void register(RegisterRequest registerRequest) {
        User user = User.builder()
                        .email(registerRequest.getEmail())
                        .role(registerRequest.getRole().name())
                        .password(passwordEncoder.encode(registerRequest.getPassword()))
                        .build();
        userRepository.save(user);
    }
}
