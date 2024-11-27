package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.AccountUpdateRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.AccountResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.AccountUpdateResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.AccountService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AccountServiceImpl implements AccountService {
    private final RestClient restClient;
    private final String ENDPOINT = "http://localhost:8081";

    public AccountServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public DataResponse<AccountResponse> findAll(String accessToken) {
        return restClient.get()
                .uri(ENDPOINT + "/api/accounts")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<AccountResponse> findById(String id, String accessToken) {
        return restClient.get()
                .uri(ENDPOINT + "/api/accounts/" + id)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<AccountUpdateResponse> update(AccountUpdateRequest accountUpdateRequest, String accessToken) {
        return restClient.put()
                .uri(ENDPOINT + "/api/accounts")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(accountUpdateRequest)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<AccountUpdateResponse> updateWithoutPassword(AccountUpdateRequest accountUpdateRequest, String accessToken) {
        return restClient.put()
                .uri(ENDPOINT + "/api/accounts/update-account-without-password")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(accountUpdateRequest)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public boolean existsByEmail(String email, String accessToken) {
        return Boolean.TRUE.equals(restClient.get()
                .uri(ENDPOINT + "/api/accounts/exists-by-email/" + email)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Boolean.class));
    }
}
