package iuh.fit.se.techgalaxy.frontend.admin.dto.request;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.SystemUserResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.OrderStatus;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    String id;
    String customerId;
    SystemUserResponse systemUserId;
    String address;
    PaymentStatus paymentStatus;
    OrderStatus orderStatus;
}
