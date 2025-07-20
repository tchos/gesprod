package tchos.gesprod.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "API pour l'authentification")
public class AuthenticationController {

    private final AuthenticationService service;

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
    public void register(@RequestBody RegisterRequest registerRequest) {
        service.register(registerRequest);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authentifier un utilisateur",
            description = "Cette méthode permet à un utilisateur de se connecter à l'API",
            operationId = "register",
            tags = {"Authentification"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Connexion effectuée avec succès")
            }
    )
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authRequest) {
        return service.authenticate(authRequest);
    }
}
