package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantDetailRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductVariantDetailResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductVariantResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private RestClient restClient;
    private ObjectMapper objectMapper;
    private static final String ENDPOINT = "http://localhost:8081";

    public ProductServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public DataResponse<ProductResponse> createProduct(ProductRequest productRequest) {
        DataResponse<ProductResponse> productResponse = restClient.post()
                .uri(ENDPOINT + "/products")
                .body(productRequest)
                .exchange((request, response) -> {
                            DataResponse<ProductResponse> productResponseDataResponse = null;
                            if (response.getBody().available() > 0) {
                                productResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                                });
                            }
                            assert productResponseDataResponse != null;
                            return productResponseDataResponse;
                        }
                );
        return productResponse;
    }

    @Override
    public DataResponse<ProductVariantResponse> createVariant(String productId, ProductVariantRequest variantRequest) {
        DataResponse<ProductVariantResponse> variantResponse = restClient.post()
                .uri(ENDPOINT + "/products/" + productId + "/variants")
                .body(variantRequest)
                .exchange((request, response) -> {
                            System.out.println(response.getStatusCode());
                            System.out.println(response.getBody());
                            DataResponse<ProductVariantResponse> productVariantResponseDataResponse = null;

                            if (response.getBody().available() > 0) {
                                productVariantResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                                });
                            }
                            assert productVariantResponseDataResponse != null;
                            return productVariantResponseDataResponse;
                        }
                );
        return variantResponse;
    }


    @Override
    public DataResponse<ProductVariantDetailResponse> createVariantDetail(String variantId, List<ProductVariantDetailRequest> detailRequest) {
        DataResponse<ProductVariantDetailResponse> detailResponse = restClient.post()
                .uri(ENDPOINT + "/variants/" + variantId + "/details")
                .body(detailRequest)
                .exchange((request, response) -> {
                            System.out.println(response.getStatusCode());
                            System.out.println(response.getBody());
                            if (response.getBody().available() > 0) {
                                System.out.println(new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
                            }
                            DataResponse<ProductVariantDetailResponse> detailResponseDataResponse = null;
                            if (response.getBody().available() > 0) {
                                detailResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                                });
                            }
                            System.out.println("detailResponseDataResponse: " + detailResponseDataResponse);
                            assert detailResponseDataResponse != null;
                            return detailResponseDataResponse;
                        }
                );
        return detailResponse;
    }

}
