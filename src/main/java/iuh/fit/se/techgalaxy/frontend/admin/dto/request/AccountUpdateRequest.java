package iuh.fit.se.techgalaxy.frontend.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequest {
    private String id;
    private String email;
    private String password;
    private List<String> rolesIds;

}
