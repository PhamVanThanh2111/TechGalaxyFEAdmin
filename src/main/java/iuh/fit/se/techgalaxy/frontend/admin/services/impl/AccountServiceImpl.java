package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.AccountResponse;
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
    public DataResponse<AccountResponse> findAll() {
        return restClient.get()
                .uri(ENDPOINT + "/api/accounts")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
