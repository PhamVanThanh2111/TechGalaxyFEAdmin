package iuh.fit.se.techgalaxy.frontend.admin.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductsImage {
    private String id;
    private String path;
    private ProductVariantDetail productVariantDetail;
}
