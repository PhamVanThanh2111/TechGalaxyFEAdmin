package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.SystemUserRequestDTO;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.UserRegisterRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.CustomerResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.SystemUserResponseDTO;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.UserRegisterResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.Gender;
import iuh.fit.se.techgalaxy.frontend.admin.services.SystemUserService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class SystemUserServiceImpl implements SystemUserService {
    private RestClient restClient;
    private ObjectMapper objectMapper;
    private static final String ENDPOINT = "http://localhost:8081";

    public SystemUserServiceImpl(ObjectMapper objectMapper, RestClient restClient) {
        this.objectMapper = objectMapper;
        this.restClient = restClient;
    }

    @Override
    public DataResponse<SystemUserResponseDTO> findAll() {
        return restClient.get()
                .uri(ENDPOINT + "/system-users/all")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<SystemUserResponseDTO> findById(String id) {
        return restClient.get()
                .uri(ENDPOINT + "/system-users/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<SystemUserResponseDTO> create(SystemUserRequestDTO systemUserRequestDTO) {
        if (systemUserRequestDTO.getAvatar() == null || systemUserRequestDTO.getAvatar().isEmpty()) {
            if (systemUserRequestDTO.getGender() != null) {
                if (systemUserRequestDTO.getGender() == Gender.MALE) {
                    // set MultipartFile avatar
                    systemUserRequestDTO.setAvatar("undraw_profile.svg");
                } else if (systemUserRequestDTO.getGender() == Gender.FEMALE) {
                    systemUserRequestDTO.setAvatar("undraw_profile_1.svg");
                }
            } else {
                systemUserRequestDTO.setAvatar("undraw_profile.svg");
            }
        }

        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail(systemUserRequestDTO.getAccount().getEmail());
        userRegisterRequest.setPassword("123456");
        userRegisterRequest.setFullName(systemUserRequestDTO.getName());

        DataResponse<UserRegisterResponse> accountResponse = restClient.post()
                .uri(ENDPOINT + "/api/accounts/auth/create-account")
                .accept(MediaType.APPLICATION_JSON)
                .body(userRegisterRequest)
                .exchange((request, response) -> {
                    DataResponse<UserRegisterResponse> dataAccountResponse = null;
                    if (response.getBody().available() > 0) {
                        dataAccountResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                    }
                    assert dataAccountResponse != null;
                    return dataAccountResponse;
                });

        if (accountResponse.getData() == null) {
            throw new RuntimeException("Failed to register account.");
        }

        List<UserRegisterResponse> list = (List<UserRegisterResponse>) accountResponse.getData();
        UserRegisterResponse userRegisterResponse = list.get(0);

        SystemUserResponseDTO.AccountResponse account = new SystemUserResponseDTO.AccountResponse();
        account.setId(userRegisterResponse.getId());

        return restClient.post()
                .uri(ENDPOINT + "/system-users")
                .accept(MediaType.APPLICATION_JSON)
                .body(systemUserRequestDTO)
                .exchange((request, response) -> {
                    DataResponse<SystemUserResponseDTO> dataResponse = null;
                    if (response.getBody().available() > 0) {
                        dataResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {
                        });
                    }
                    assert dataResponse != null;
                    return dataResponse;
                });
    }

    @Override
    public DataResponse<SystemUserResponseDTO> update(SystemUserRequestDTO systemUserRequestDTO) {
        if (systemUserRequestDTO.getAvatar() == null || systemUserRequestDTO.getAvatar().isEmpty()) {
            if (systemUserRequestDTO.getGender() != null) {
                if (systemUserRequestDTO.getGender() == Gender.MALE) {
                    systemUserRequestDTO.setAvatar("undraw_profile.svg");
                } else if (systemUserRequestDTO.getGender() == Gender.FEMALE) {
                    systemUserRequestDTO.setAvatar("undraw_profile_1.svg");
                }
            } else {
                systemUserRequestDTO.setAvatar("undraw_profile.svg");
            }
        }

        return restClient.put()
                .uri(ENDPOINT + "/system-users/" + systemUserRequestDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .body(systemUserRequestDTO)
                .exchange((request, response) -> {
                    DataResponse<SystemUserResponseDTO> dataResponse = null;
                    if (response.getBody().available() > 0) {
                        dataResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                    }
                    assert dataResponse != null;
                    return dataResponse;
                });
    }
}
