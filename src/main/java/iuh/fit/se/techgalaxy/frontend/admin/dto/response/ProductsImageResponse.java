package iuh.fit.se.techgalaxy.frontend.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductsImageResponse {
    String id;
    String path;
    Boolean avatar;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
