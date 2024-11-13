package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.CustomerRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.UserRegisterRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.CustomerResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.UserRegisteResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.Gender;
import iuh.fit.se.techgalaxy.frontend.admin.services.CustomerService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private static final String ENDPOINT = "http://localhost:8081";

    public CustomerServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public DataResponse<CustomerResponse> findByEmail(String email) {
        return null;
    }

    @Override
    public DataResponse<CustomerResponse> findAll() {
        return restClient.get()
                .uri(ENDPOINT + "/customers")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public DataResponse<CustomerResponse> findById(String id) {
        return restClient.get()
                .uri(ENDPOINT + "/customers/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    // save account and customer information
    @Override
    public DataResponse<CustomerResponse> save(CustomerRequest customerRequest) {
        if (customerRequest.getAvatar() == null || customerRequest.getAvatar().isEmpty()) {
            if (customerRequest.getGender() != null) {
                if (customerRequest.getGender() == Gender.MALE) {
                    customerRequest.setAvatar("undraw_profile.svg");
                } else if (customerRequest.getGender() == Gender.FEMALE) {
                    customerRequest.setAvatar("undraw_profile_1.svg");
                }
            } else {
                customerRequest.setAvatar("undraw_profile.svg");
            }
        }
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail(customerRequest.getEmail());
        userRegisterRequest.setPassword("123456");
        userRegisterRequest.setFullName(customerRequest.getName());

        DataResponse<UserRegisteResponse> accountResponse = restClient.post()
                .uri(ENDPOINT + "/api/accounts/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .body(userRegisterRequest)
                .exchange((request, response) -> {
                    DataResponse<UserRegisteResponse> dataAccountResponse = null;
                    if (response.getBody().available() > 0) {
                        dataAccountResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                    }
                    assert dataAccountResponse != null;
                    return dataAccountResponse;
                });

        if (accountResponse.getData() == null) {
            throw new RuntimeException("Failed to register account.");
        }

        return restClient.post()
                .uri(ENDPOINT + "/customers")
                .accept(MediaType.APPLICATION_JSON)
                .body(customerRequest)
                .exchange((request, response) -> {
                    DataResponse<CustomerResponse> dataResponse = null;
                    if (response.getBody().available() > 0) {
                        dataResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {
                        });
                    }
                    assert dataResponse != null;
                    return dataResponse;
                });
    }

    @Override
    public DataResponse<CustomerResponse> update(String id, CustomerRequest customerRequest) {
        return null;
    }

    @Override
    public DataResponse<Boolean> delete(String id) {
        return null;
    }
}
