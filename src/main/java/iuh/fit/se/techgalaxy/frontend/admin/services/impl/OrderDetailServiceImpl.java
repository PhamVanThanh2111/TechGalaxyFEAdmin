package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.OrderDetailRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.OrderDetailResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String ENDPOINT = "http://localhost:8081";

    @Autowired
    public OrderDetailServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public DataResponse<OrderDetailResponse> getOrderDetail(String orderId) {
        return restClient.get()
                .uri(ENDPOINT + "/order-details/order/" + orderId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<OrderDetailResponse> createOrderDetail(OrderDetailRequest orderDetailRequest) {
        return restClient.post()
                .uri(ENDPOINT + "/order-details")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(orderDetailRequest)
                .exchange((request, response)->{
                    DataResponse<OrderDetailResponse> dataAccountResponse = null;
                    if (response.getBody().available() > 0) {
                        dataAccountResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                    }
                    assert dataAccountResponse != null;
                    return dataAccountResponse;
                });
    }
}
