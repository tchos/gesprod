package tchos.gesprod.security;
// 9

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UtilisateurRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        /* Le DaoAuthenticationProvider utilise le UserDetailsService
        et le PasswordEncoder pour vérifier les identifiants de l'utilisateur. */
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        try {
            return config.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        /* encoder les mots de passe avant de les enregistrer
        et pour les comparer lors de la connexion. */
        return new BCryptPasswordEncoder();
    }
}
