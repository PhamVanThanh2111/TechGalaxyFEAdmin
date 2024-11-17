package iuh.fit.se.techgalaxy.frontend.admin.dto.request;

import iuh.fit.se.techgalaxy.frontend.admin.entities.Role;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.Gender;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.SystemUserLevel;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.SystemUserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemUserRequestDTO {
    private String id;
    @NotBlank(message = "NAME_NOT_EMPTY")
    private String name;

    @NotBlank(message = "PHONE_NOT_EMPTY")
    @Pattern(regexp = "^0[0-9]{9}$", message = "PHONE_INVALID")
    private String phone;
    @NotBlank(message = "ADDRESS_NOT_EMPTY")
    @Pattern(regexp = "^[a-zA-Z0-9\\s,.'-]{1,100}$", message = "ADDRESS_INVALID")
    private String address;

    @NotNull(message = "SYSTEM_USER_STATUS_NOT_EMPTY")
    private SystemUserStatus systemUserStatus;

    @NotNull(message = "LEVEL_NOT_EMPTY")
    private SystemUserLevel level;

    @NotNull(message = "GENDER_NOT_EMPTY")
    private Gender gender;

    private String avatar;

    private AccountRequest account;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountRequest {
        private String id;
        private String email;
        private String password;
        private List<Role> roles;
    }
}
