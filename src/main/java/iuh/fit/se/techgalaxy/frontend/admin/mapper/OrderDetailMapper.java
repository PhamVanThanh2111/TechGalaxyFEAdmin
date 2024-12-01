package iuh.fit.se.techgalaxy.frontend.admin.mapper;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.OrderDetailRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.OrderDetailResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);

    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);

    OrderDetailRequest toOrderDetailRequest(OrderDetail orderDetail);

    OrderDetail toOrderDetailFromRequest(OrderDetailRequest orderDetailRequest);

    OrderDetail toOrderDetailFromResponse(OrderDetailResponse orderDetailResponse);
}
