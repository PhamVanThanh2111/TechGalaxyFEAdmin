package iuh.fit.se.techgalaxy.frontend.admin.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
     String id;
     @Size(min = 5, max = 24, message = "PRODUCT_NAME_INVALID")
     String name;
     String trademarkId;
}
