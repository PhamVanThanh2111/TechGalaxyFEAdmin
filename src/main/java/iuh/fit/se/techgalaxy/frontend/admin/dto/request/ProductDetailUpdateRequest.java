package iuh.fit.se.techgalaxy.frontend.admin.dto.request;

import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailUpdateRequest {
    ProductStatus status;
    Integer quantity;
}
