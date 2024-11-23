package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.CustomerRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.UserRegisterRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.CustomerResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.UserRegisterResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Account;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.Gender;
import iuh.fit.se.techgalaxy.frontend.admin.services.CustomerService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private static final String ENDPOINT = "http://localhost:8081";

    private static final String DEFAULT_MALE_AVATAR = "undraw_profile.svg";
    private static final String DEFAULT_FEMALE_AVATAR = "undraw_profile_1.svg";

    private void setDefaultAvatarIfMissing(CustomerRequest customerRequest) {
        if (isAvatarMissing(customerRequest)) {
            customerRequest.setAvatar(getAvatarBasedOnGender(customerRequest));
        }
    }

    private boolean isAvatarMissing(CustomerRequest customerRequest) {
        return customerRequest.getAvatar() == null || customerRequest.getAvatar().isEmpty();
    }

    private String getAvatarBasedOnGender(CustomerRequest customerRequest) {
        if (customerRequest.getGender() == Gender.FEMALE) {
            return DEFAULT_FEMALE_AVATAR;
        }
        return DEFAULT_MALE_AVATAR;
    }

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
                .body(new ParameterizedTypeReference<>() {});
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
    public DataResponse<CustomerResponse> save(CustomerRequest customerRequest) throws JsonProcessingException {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail(customerRequest.getAccount().getEmail());
        userRegisterRequest.setPassword("123456");
        userRegisterRequest.setFullName(customerRequest.getName());

        DataResponse<UserRegisterResponse> accountResponse = restClient.post()
                .uri(ENDPOINT + "/api/accounts/auth/create-customer-account")
                .contentType(MediaType.APPLICATION_JSON)
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

        Account account = new Account();
        account.setId(userRegisterResponse.getId());
        customerRequest.setAccount(account);

        if (customerRequest.getPhone().isEmpty())
            customerRequest.setPhone(null);

        setDefaultAvatarIfMissing(customerRequest);

        return restClient.post()
                .uri(ENDPOINT + "/customers")
                .accept(MediaType.APPLICATION_JSON)
                .body(customerRequest)
                .exchange((request, response) -> {
                    String responseBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    System.out.println("Response Body: " + responseBody);

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
    public DataResponse<CustomerResponse> update(CustomerRequest customerRequest) {
        setDefaultAvatarIfMissing(customerRequest);
        return restClient.put()
                .uri(ENDPOINT + "/customers/" + customerRequest.getId())
                .accept(MediaType.APPLICATION_JSON)
                .body(customerRequest)
                .exchange((request, response) -> {
                    String responseBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    System.out.println("Response Body: " + responseBody);

                    DataResponse<CustomerResponse> dataResponse = null;
                    if (response.getBody().available() > 0) {
                        dataResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                    }
                    assert dataResponse != null;
                    return dataResponse;
                });
    }

    @Override
    public DataResponse<Boolean> delete(String id) {
        return restClient.delete()
                .uri(ENDPOINT + "/customers/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
