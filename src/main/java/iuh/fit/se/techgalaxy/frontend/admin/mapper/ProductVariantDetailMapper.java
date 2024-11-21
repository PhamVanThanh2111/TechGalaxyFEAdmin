package iuh.fit.se.techgalaxy.frontend.admin.mapper;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductDetailUpdateRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantDetailRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductVariantDetailResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Color;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Memory;
import iuh.fit.se.techgalaxy.frontend.admin.entities.ProductVariant;
import iuh.fit.se.techgalaxy.frontend.admin.entities.ProductVariantDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductVariantDetailMapper {
    @Mapping(target = "memories", expression = "java(mapMemories(productVariantDetails))")
    ProductVariantDetailResponse toProductVariantDetailResponse(ProductVariantDetail productVariantDetail, List<ProductVariantDetail> productVariantDetails);

    default Map<String, ProductVariantDetailResponse.ColorQuantity[]> mapMemories(List<ProductVariantDetail> productVariantDetails) {
        return productVariantDetails.stream()
                .collect(Collectors.groupingBy(
                        detail -> detail.getMemory().getId(),
                        Collectors.mapping(detail ->
                                        new ProductVariantDetailResponse.ColorQuantity(
                                                detail.getViewsCount(),
                                                detail.getPrice(),
                                                detail.getSale(),
                                                detail.getQuantity(),
                                                detail.getColor().getId()
                                        ),
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().toArray(new ProductVariantDetailResponse.ColorQuantity[0])
                ));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "quantity", source = "detail.quantity")
    @Mapping(target = "price", source = "requestDTO.price")
    @Mapping(target = "sale", source = "requestDTO.sale")
    @Mapping(target = "productVariant", source = "productVariant")
    ProductVariantDetail toProductVariantDetail(ProductVariantDetailRequest requestDTO,
                                                ProductVariantDetailRequest.ColorRequest detail,
                                                Color color,
                                                Memory memory,
                                                ProductVariant productVariant);
    void  toUpdate(@MappingTarget ProductVariantDetail productVariantDetail, ProductDetailUpdateRequest productDetailUpdateRequest);
}
