package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.UploadFileResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class FileServiceImpl implements FileService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private static final String ENDPOINT = "http://localhost:8081";

    public FileServiceImpl(RestClient restClient, ObjectMapper objectMapper, ObjectMapper objectMapper1) {
        this.restClient = restClient;
        this.objectMapper = objectMapper1;
    }

    @Override
    public DataResponse<UploadFileResponse> uploadFile(MultipartFile file,String folder) throws IOException, URISyntaxException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });
        body.add("folder", folder);
        return restClient.post()
                .uri(ENDPOINT + "/file")
                .body(body)
                .exchange((request, response) -> {
                    System.out.println("Upload 1 file");
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());
                    DataResponse<UploadFileResponse> uploadFileResponseDataResponse = null;
                    if (response.getBody().available() > 0) {
                        uploadFileResponseDataResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                    }
                    assert uploadFileResponseDataResponse != null;
                    return uploadFileResponseDataResponse;
                });
    }

    @Override
    public DataResponse<UploadFileResponse> uploadFiles(MultipartFile[] files,String folder) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (MultipartFile file : files) {
            body.add("files", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
        }
        body.add("folder", folder);
        return restClient.post()
                .uri(ENDPOINT + "/files")
                .body(body)
                .exchange((request, response) -> {
                    System.out.println("Upload many file");
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());
                    DataResponse<UploadFileResponse> uploadFileResponseDataResponse = null;
                    if (response.getBody().available() > 0) {
                        uploadFileResponseDataResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                    }
                    assert uploadFileResponseDataResponse != null;
                    return uploadFileResponseDataResponse;
                });

    }
}
