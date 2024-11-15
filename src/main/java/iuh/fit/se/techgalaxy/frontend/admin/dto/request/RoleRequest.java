package iuh.fit.se.techgalaxy.frontend.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class RoleRequest {
    private String id;
    @NotBlank(message = "NAME_NOT_EMPTY")
    private String name;
    private boolean active;
    private String description;
    private List<String> permissionIds;
}