package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductDetailUpdateRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantDetailRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.*;

import java.util.List;

public interface ProductService {
    DataResponse<ProductResponse> getProductById(String productId);

    DataResponse<ProductResponse> getAllProducts(String accessToken);

    DataResponse<ProductVariantResponse> getVariantById(String variantId);

    DataResponse<ProductDetailResponse> getVariantDetailById(String detailId);

    DataResponse<ProductResponse> createProduct(ProductRequest productRequest);

    DataResponse<ProductVariantResponse> createVariant(String productId, ProductVariantRequest variantRequest);

    DataResponse<ProductVariantDetailResponse> createVariantDetail(String variantId, List<ProductVariantDetailRequest> detailRequest);

    DataResponse<ProductResponse> updateProduct(String productId, ProductRequest productRequest);

    DataResponse<ProductVariantResponse> updateVariant(String variantId, ProductVariantRequest variantRequest);

    DataResponse<Boolean> updateVariantDetail(String detailId, ProductDetailUpdateRequest detailRequest);

    DataResponse<Object> deleteProduct(String productId);

    DataResponse<Object> deleteVariant(String variantId);

    DataResponse<Object> deleteVariantDetail(String detailId);

    DataResponse<ProductVariantResponse> getAllProductVariantsByProductId(String productId);

    DataResponse<ProductVariantDetailResponse> getAllVariantDetailsByVariantId(String variantId);

//    DataResponse<ProductVariantResponse> getAllVariants();

    DataResponse<ProductVariantResponse> findProductVariantByProductVariantDetailId(String detailId);
}
