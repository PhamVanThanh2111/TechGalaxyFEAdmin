package iuh.fit.se.techgalaxy.frontend.admin.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

public interface FileService {
    String uploadFile(String folder, MultipartFile file) throws IOException, URISyntaxException;

}
