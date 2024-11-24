package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.AttributeValueRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.AttributeResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ValueResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.AttributeService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

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
                .uri(ENDPOINT + "/attributes/attributeByVariantId/" + variantId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public DataResponse<AttributeResponse> getAllAttribute() {
        return restClient.get()
                .uri(ENDPOINT + "/attributes")
                .exchange((request, response) -> {
                            System.out.println("getAllAttribute");
                            System.out.println(response.getStatusCode());
                            DataResponse<AttributeResponse> attributeResponseDataResponse = null;
                            if (response.getBody().available() > 0) {
                                attributeResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                                });
                            }
                            assert attributeResponseDataResponse != null;
                            return attributeResponseDataResponse;
                        }
                );
    }

    @Override
    public DataResponse<AttributeResponse> getAttributeById(String id) {
        return restClient.get()
                .uri(ENDPOINT + "/attributes/attributes/" + id)
                .exchange((request, response) -> {
                            System.out.println("getAttributeById");
                            System.out.println(response.getStatusCode());
                            DataResponse<AttributeResponse> attributeResponseDataResponse = null;
                            if (response.getBody().available() > 0) {
                                attributeResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                                });
                            }
                            assert attributeResponseDataResponse != null;
                            return attributeResponseDataResponse;
                        }
                );
    }

    @Override
    public DataResponse<Object> createAttributeValueByVariantId(String variantId, List<AttributeValueRequest> values) {
        values.forEach(System.out::println);
        return restClient.post()
                .uri(ENDPOINT + "/attributes/productvariant/" + variantId)
                .body(values)
                .exchange((request, response) -> {
                    System.out.println("createAttributeValueByVariantId");
                    System.out.println(response.getStatusCode());
                    DataResponse<Object> attributeResponseDataResponse = null;
                    if (response.getBody().available() > 0) {
                        attributeResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                        });
                    }
                    assert attributeResponseDataResponse != null;
                    return attributeResponseDataResponse;
                });
    }

    @Override
    public DataResponse<ValueResponse> getValueByNameAtribute(String name) {
        return restClient.get()
                .uri(ENDPOINT + "/attributes/atributevalue/" + name)
                .exchange((request, response) -> {

                            System.out.println("getValueByNameAtribute");
                            DataResponse<ValueResponse> attributeResponseDataResponse = null;
                            if (response.getBody().available() > 0) {
                                attributeResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                                });
                            }
                            assert attributeResponseDataResponse != null;
                            return attributeResponseDataResponse;
                        }
                );
    }
}
