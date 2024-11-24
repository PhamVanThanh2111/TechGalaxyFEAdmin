package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.OrderDetailResponse;

public interface OrderDetailService {
    DataResponse<OrderDetailResponse> getOrderDetail(String orderId);
}
