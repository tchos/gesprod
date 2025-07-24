package tchos.gesprod.security;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tchos.gesprod.security.AuthRequest;
import tchos.gesprod.security.AuthResponse;
import tchos.gesprod.security.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "API pour l'authentification")
public class AuthController {

    private final AuthService authService;

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
    public AuthResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authentifier un utilisateur",
            description = "Cette méthode permet à un utilisateur de se connecter à l'API",
            operationId = "login",
            tags = {"Authentification"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Connexion effectuée avec succès")
            }
    )
    public AuthResponse login(
            @RequestBody AuthRequest authRequest
    ) {
        return authService.authenticate(authRequest);
    }
}