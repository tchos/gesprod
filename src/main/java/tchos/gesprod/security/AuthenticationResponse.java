package tchos.gesprod.security;
//5

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;  // JWT (peut être null si compte non activé)
    private String message; // Message de statut

    // Exemple de constructeur pour les réponses sans token
    public static AuthenticationResponse withMessage(String message) {
        return AuthenticationResponse.builder()
                .message(message)
                .build();
    }
}
