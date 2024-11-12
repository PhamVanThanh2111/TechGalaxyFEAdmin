package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.SystemUserResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.SystemUserService;
import org.springframework.web.client.RestClient;

public class SystemUserServiceImpl implements SystemUserService {
    private RestClient restClient;
    private ObjectMapper objectMapper;
    private static final String ENDPOINT = "http://localhost:8081";

    public SystemUserServiceImpl(ObjectMapper objectMapper, RestClient restClient) {
        this.objectMapper = objectMapper;
        this.restClient = restClient;
    }

    @Override
    public DataResponse<SystemUserResponse> findAll() {
        return null;
    }
}
