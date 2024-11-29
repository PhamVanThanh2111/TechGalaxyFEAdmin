package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.TrademarkRequest;
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

    public TrademarkServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public DataResponse<TrademarkResponse> getAllTrademarks() {
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
    public DataResponse<TrademarkResponse> save(String name, String accessToken) {
        return restClient.post()
                .uri(ENDPOINT + "/trademarks?name=" + name)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<Object> delete(String id, String accessToken) {
        return restClient.delete()
                .uri(ENDPOINT + "/trademarks/" + id)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<TrademarkResponse> update(TrademarkRequest trademarkRequest, String accessToken) {
        return restClient.put()
                .uri(ENDPOINT + "/trademarks")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .body(trademarkRequest)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
