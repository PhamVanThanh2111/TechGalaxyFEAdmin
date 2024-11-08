package iuh.fit.se.techgalaxy.frontend.admin.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.DetailStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private String id;
    private DetailStatus detailStatus;
    private Order order;
    private ProductVariantDetail productVariantDetail;
    private Integer quantity;
    private Double price;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

}
