package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.SystemUserRequestDTO;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.SystemUserResponseDTO;

public interface SystemUserService {
    DataResponse<SystemUserResponseDTO> findAll();

    DataResponse<SystemUserResponseDTO> findById(String id);

    DataResponse<SystemUserResponseDTO> create(SystemUserRequestDTO systemUserRequestDTO);

    DataResponse<SystemUserResponseDTO> update(SystemUserRequestDTO systemUserRequestDTO);
}
