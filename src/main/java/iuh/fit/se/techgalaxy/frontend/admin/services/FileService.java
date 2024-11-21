package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.UploadFileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    DataResponse<UploadFileResponse> uploadFile(MultipartFile file);
}
