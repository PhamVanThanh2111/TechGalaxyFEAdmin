package iuh.fit.se.techgalaxy.frontend.admin.entities;

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
public class SystemUser {
    private String id;
    private SystemUserStatus systemUserStatus;
    private SystemUserLevel level;
    private Account account;
    private String name;
    private String address;
    private String phone;
    private Gender gender;
    private String avatar;
    private List<Order> order;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
