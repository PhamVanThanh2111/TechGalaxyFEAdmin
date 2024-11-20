package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Memory;
import iuh.fit.se.techgalaxy.frontend.admin.services.MemoryService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class MemoryServiceImpl implements MemoryService {

    private RestClient restClient;
    private ObjectMapper objectMapper;
    private static final String ENDPOINT = "http://localhost:8081";

    public MemoryServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public DataResponse<Memory> getAllMemories() {
        return restClient.get()
                .uri(ENDPOINT + "/memories")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public  DataResponse<Memory> getMemoryById(String id) {
        return restClient.get()
                .uri(ENDPOINT + "/memories/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
