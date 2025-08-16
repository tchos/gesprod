package tchos.gesprod.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Retourne un 404 quand l'exception est levée
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Utilisateur non trouvé");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
