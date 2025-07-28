package tchos.gesprod.security;
//13

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "API pour l'authentification")
public class AuthenticationController {

    private final AuthenticationService service;
    private final LogoutService logoutService;

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
    ) {
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

}
