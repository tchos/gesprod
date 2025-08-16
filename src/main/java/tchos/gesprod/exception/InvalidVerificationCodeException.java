package tchos.gesprod.exception;

public class InvalidVerificationCodeException extends RuntimeException {
    public InvalidVerificationCodeException() {
        super("Code de vérification invalide ou expiré");
    }
}
