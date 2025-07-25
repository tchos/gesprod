package tchos.gesprod.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotNull @NotBlank private String email;
    @NotNull private String firstname;
    @NotNull private String lastname;
    @NotNull private String password;
    @NotNull private UserRole role;
}
