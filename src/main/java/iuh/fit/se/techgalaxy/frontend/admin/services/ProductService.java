package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantDetailRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductVariantDetailResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductVariantResponse;

public interface ProductService {
    public DataResponse<ProductResponse> createProduct(ProductRequest productRequest);
    public DataResponse<ProductVariantResponse> createVariant(String productId, ProductVariantRequest variantRequest);
    public DataResponse<ProductVariantDetailResponse> createVariantDetail(String variantId, ProductVariantDetailRequest detailRequest);

    }
