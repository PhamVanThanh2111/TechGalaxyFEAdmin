package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.SystemUserRequestDTO;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.SystemUserResponseDTO;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.UploadFileResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.SystemUser;
import org.springframework.web.multipart.MultipartFile;

public interface SystemUserService {
    DataResponse<SystemUserResponseDTO> findAll(String accessToken);

    DataResponse<SystemUserResponseDTO> findById(String id, String accessToken);

    DataResponse<SystemUserResponseDTO> findByEmail(String email, String accessToken);

    DataResponse<SystemUserResponseDTO> create(SystemUserRequestDTO systemUserRequestDTO, String accessToken);

    DataResponse<SystemUserResponseDTO> update(SystemUserRequestDTO systemUserRequestDTO, String accessToken);

    DataResponse<Void> delete(String id, String accessToken);
}
