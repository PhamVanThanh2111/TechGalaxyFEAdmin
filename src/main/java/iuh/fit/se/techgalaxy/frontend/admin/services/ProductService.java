package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantDetailRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductVariantDetailResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductVariantResponse;

import java.util.List;

public interface ProductService {
    public DataResponse<ProductResponse> getAllProducts();
    public DataResponse<ProductResponse> getProductById(String productId);
    public DataResponse<ProductVariantResponse> getVariantById(String variantId);
    public DataResponse<ProductVariantDetailResponse> getVariantDetailById(String detailId);


    public DataResponse<ProductResponse> createProduct(ProductRequest productRequest);
    public DataResponse<ProductVariantResponse> createVariant(String productId, ProductVariantRequest variantRequest);
    public DataResponse<ProductVariantDetailResponse> createVariantDetail(String variantId, List<ProductVariantDetailRequest> detailRequest);

    public DataResponse<ProductResponse> updateProduct(String productId, ProductRequest productRequest);

    public DataResponse<ProductVariantResponse> updateVariant(String variantId, ProductVariantRequest variantRequest);

    public DataResponse<ProductVariantDetailResponse> updateVariantDetail(String detailId, ProductVariantDetailRequest detailRequest);

    public DataResponse<Object> deleteProduct(String productId);
    public DataResponse<Object> deleteVariant(String variantId);
    public DataResponse<Object> deleteVariantDetail(String detailId);

    public DataResponse<ProductVariantResponse> getAllProductVariantsByProductId(String productId);
    public DataResponse<ProductVariantDetailResponse> getAllVariantDetailsByVariantId(String variantId);

    public DataResponse<ProductVariantResponse> getAllVariants();

}