package iuh.fit.se.techgalaxy.frontend.admin.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class ProductFullRequest {
    String name;
    String trademarkId;
    List<ProductVariantRequest> variants;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @ToString
    public static class ProductVariantRequest {
        String name;
        String description;
        String content;
        String avatar;
        Boolean featured;
        String usageCategoryId;
        List<ProductVariantDetailRequest> details;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @FieldDefaults(level = AccessLevel.PRIVATE)
        @ToString
        public static class ProductVariantDetailRequest {
            String memid;
            Double price;
            Double sale;
            List<ColorRequest> colors;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            @FieldDefaults(level = AccessLevel.PRIVATE)
            @ToString
            public static class ColorRequest {
                String colorId;
                Integer quantity;
            }
        }
    }
}
