package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ValueResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Value;
import iuh.fit.se.techgalaxy.frontend.admin.services.AttributeService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
@Service
public class AttributeServiceImpl implements AttributeService {


    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private static final String ENDPOINT = "http://localhost:8081";

    public AttributeServiceImpl(RestClient restClient, ObjectMapper objectMapper, ObjectMapper objectMapper1) {
        this.restClient = restClient;
        this.objectMapper = objectMapper1;
    }
    @Override
    public DataResponse<ValueResponse> getAttributesByVariantId(String variantId) {
        return restClient.get()
                .uri(ENDPOINT + "/attributes/attributeByVariantId/"+variantId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
