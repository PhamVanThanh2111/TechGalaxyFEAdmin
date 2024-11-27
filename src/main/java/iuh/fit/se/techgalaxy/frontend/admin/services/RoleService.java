package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.RoleResponse;

public interface RoleService {
    DataResponse<RoleResponse> findAll(String accessToken);

    DataResponse<RoleResponse> findById(String id, String accessToken);

    DataResponse<RoleResponse> getRoleByEmail(String email, String accessToken);
}
