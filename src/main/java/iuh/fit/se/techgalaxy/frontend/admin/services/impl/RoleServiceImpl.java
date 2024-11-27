package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.RoleResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.RoleService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class RoleServiceImpl implements RoleService {
    private final RestClient restClient;
    private final String ENDPOINT = "http://localhost:8081";

    public RoleServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public DataResponse<RoleResponse> findAll(String accessToken) {
        return restClient.get()
                .uri(ENDPOINT + "/roles/add")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<RoleResponse> findById(String id, String accessToken) {
        return restClient.get()
                .uri(ENDPOINT + "/roles/" + id)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<RoleResponse> getRoleByEmail(String email, String accessToken) {
        return restClient.get()
                .uri(ENDPOINT + "/roles/email/" + email)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
