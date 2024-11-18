package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.UploadFileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

public interface FileService {
    public DataResponse<UploadFileResponse> uploadFile(MultipartFile file,String folder) throws IOException, URISyntaxException;


    public DataResponse<UploadFileResponse> uploadFiles(MultipartFile[] files, String folder) throws IOException, URISyntaxException;
}
