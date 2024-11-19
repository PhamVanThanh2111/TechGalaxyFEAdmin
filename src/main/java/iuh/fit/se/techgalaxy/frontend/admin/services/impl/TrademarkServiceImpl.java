package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.TrademarkResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.TrademarkService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TrademarkServiceImpl implements TrademarkService {

    private final RestClient restClient;
    private static final String ENDPOINT = "http://localhost:8081";

    public TrademarkServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
    }

    @Override
    public DataResponse<TrademarkResponse> findAll() {
        return restClient.get()
                .uri(ENDPOINT + "/trademarks")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public DataResponse<TrademarkResponse> findById(String id) {
        return restClient.get()
                .uri(ENDPOINT + "/trademarks/id/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<TrademarkResponse> save(String name) {
        return restClient.post()
                .uri(ENDPOINT + "/trademarks?name=" + name)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<Object> delete(String id) {
        return restClient.delete()
                .uri(ENDPOINT + "/trademarks/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }


}
