package iuh.fit.se.techgalaxy.frontend.admin.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class RoleRequest {
    private String id;
    private String name;
    private boolean active;
    private String description;
    private List<String> permissionIds;
}