package iuh.fit.se.techgalaxy.frontend.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteResponse {
    private String email;
    private String password;
    private String fullName;
}
