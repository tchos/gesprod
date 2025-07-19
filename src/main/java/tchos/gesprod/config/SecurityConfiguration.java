package tchos.gesprod.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactive CSRF pour les API REST
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**", // Autorise les endpoints d'authentification
                                "/swagger-ui/**", // Autorise Swagger UI
                                "/v3/api-docs/**", // Autorise la documentation OpenAPI
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/h2-console/**"
                        ).permitAll() // Autorise ces chemins sans authentification
                        .anyRequest().authenticated() // Tous les autres endpoints nécessitent une authentification
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults()); // Active l'authentification Basic pour Swagger

        return http.build();
    }
}
