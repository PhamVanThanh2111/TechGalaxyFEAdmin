package iuh.fit.se.techgalaxy.frontend.admin.mapper;


import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductVariantResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);

    ProductVariant toProductVariant(ProductVariantRequest productVariantRequest);

    void updateProductVariantFromRequest(@MappingTarget ProductVariant productVariant, ProductVariantRequest productVariantRequest);
}
