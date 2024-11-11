package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.CustomerRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.CustomerResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.CustomerService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private RestClient restClient;
    private ObjectMapper objectMapper;
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
                .body(new ParameterizedTypeReference<DataResponse<CustomerResponse>>() {});
    }

    @Override
    public DataResponse<CustomerResponse> findById(String id) {
        return null;
    }

    @Override
    public DataResponse<CustomerResponse> save(CustomerRequest customerRequest) {
        return null;
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
