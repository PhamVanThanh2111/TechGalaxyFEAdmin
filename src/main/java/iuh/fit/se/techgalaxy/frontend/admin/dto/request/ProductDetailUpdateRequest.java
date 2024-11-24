package iuh.fit.se.techgalaxy.frontend.admin.dto.request;

import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.ProductStatus;
import iuh.fit.se.techgalaxy.frontend.admin.validate.DiscountConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailUpdateRequest {
    Double price;
    @Min(value = 0, message = "Sale must be at least 0%")
    @Max(value = 1, message = "Sale cannot exceed 100%")
    @DiscountConstraint(message = "PRODUCT_DISCOUNT_INVALID")
    Double sale;
    ProductStatus status;
    Integer quantity;
}
