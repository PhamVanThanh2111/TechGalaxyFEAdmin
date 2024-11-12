package iuh.fit.se.techgalaxy.frontend.admin.dto.response;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoleResponse {
    private String id;
    private String name;
    private boolean active;
    private String description;
    private String createdAt;
    private String updatedAt;
}