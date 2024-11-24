package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

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
    private final String ENDPOINT = "http://localhost:8081";

    @Autowired
    public OrderDetailServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public DataResponse<OrderDetailResponse> getOrderDetail(String orderId) {
        return restClient.get()
                .uri(ENDPOINT + "/order-details/order/" + orderId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
