package iuh.fit.se.techgalaxy.frontend.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private String id;
    private String email;
    private String password;
    private String phone;
    private String name;
    private List<String> rolesIds;
}
