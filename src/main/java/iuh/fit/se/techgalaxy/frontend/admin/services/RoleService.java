package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.RoleResponse;

public interface RoleService {
    DataResponse<RoleResponse> findAll();

    DataResponse<RoleResponse> findById(String id);
}
