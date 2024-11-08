package iuh.fit.se.techgalaxy.frontend.admin.entities;

import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant {
    private String id;
    private String name;
    private String description;
    private String content;
    private String avatar;
    private Boolean featured;
    private ProductStatus status;
    private UsageCategory usageCategory;
    private Product product;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductVariantDetail> productVariantDetails;
    private List<ProductFeedback> feedbacks;
}
