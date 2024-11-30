package iuh.fit.se.techgalaxy.frontend.admin.dto.response;

import iuh.fit.se.techgalaxy.frontend.admin.entities.Role;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.Gender;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.SystemUserLevel;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.SystemUserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemUserResponseDTO {

    private String id;
    private SystemUserStatus systemUserStatus;
    private SystemUserLevel level;
    private String name;
    private String address;
    private String phone;
    private Gender gender;
    private String avatar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AccountResponse account;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountResponse {
        private String id;
        private String email;
        private List<Role> roles;
    }
}
