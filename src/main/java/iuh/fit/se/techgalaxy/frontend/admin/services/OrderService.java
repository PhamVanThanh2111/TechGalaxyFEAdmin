package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.OrderRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.OrderResponse;

public interface OrderService {
    DataResponse<OrderResponse> getAll();

    DataResponse<OrderResponse> create(OrderRequest orderRequest);
}
