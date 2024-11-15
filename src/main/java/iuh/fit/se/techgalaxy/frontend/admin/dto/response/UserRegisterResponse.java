package iuh.fit.se.techgalaxy.frontend.admin.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterResponse {
    @NotBlank(message = "EMAIL_NOT_EMPTY")
    private String id;
    private String email;

}
