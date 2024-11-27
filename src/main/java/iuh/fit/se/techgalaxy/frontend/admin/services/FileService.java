package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.UploadFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

public interface FileService {
    DataResponse<UploadFileResponse> uploadFile(MultipartFile file, String folder, String accessToken) throws IOException, URISyntaxException;


    DataResponse<UploadFileResponse> uploadFiles(MultipartFile[] files, String folder, String accessToken) throws IOException, URISyntaxException;
}
