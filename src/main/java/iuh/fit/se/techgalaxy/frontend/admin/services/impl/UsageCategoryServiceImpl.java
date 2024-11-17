package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.UsageCategoryRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductVariantResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.UsageCategoryResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.UsageCategoryService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class UsageCategoryServiceImpl implements UsageCategoryService {

    private RestClient restClient;
    private ObjectMapper objectMapper;
    private static final String ENDPOINT = "http://localhost:8081";

    public UsageCategoryServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }
    @Override
    public DataResponse<UsageCategoryResponse> getAllUsageCategories() {
        return restClient.get()
                .uri(ENDPOINT + "/usageCategories")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<UsageCategoryResponse> getUsageCategoryById(String id) {
        return restClient.get()
                .uri(ENDPOINT + "/usageCategories/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

}
