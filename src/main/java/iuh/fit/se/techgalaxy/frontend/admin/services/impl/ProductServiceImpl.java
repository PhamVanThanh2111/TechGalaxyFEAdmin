package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductDetailUpdateRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantDetailRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.*;
import iuh.fit.se.techgalaxy.frontend.admin.services.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private static final String ENDPOINT = "http://localhost:8081";

    public ProductServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public DataResponse<ProductResponse> createProduct(ProductRequest productRequest, String accessToken) {
        try {
            return restClient.post()
                    .uri(ENDPOINT + "/products")
                    .header("Authorization", "Bearer " + accessToken)
                    .body(productRequest)
                    .exchange((request, response) -> {
                                System.out.println(response.getStatusCode());
                                DataResponse<ProductResponse> productResponseDataResponse = null;
                                if (response.getBody().available() > 0) {
                                    productResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                                    });
                                }
                                assert productResponseDataResponse != null;
                                return productResponseDataResponse;
                            }
                    );
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            throw new RuntimeException("Session expired. Please log in again.");
        } catch (HttpClientErrorException.Forbidden e){
            System.out.println("Forbidden request: " + e.getMessage());
            throw new RuntimeException("You do not have permission to perform this action.");
        }
        catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("Unable to fetch customer data.");
        }
    }

    @Override
    public DataResponse<ProductVariantResponse> createVariant(String productId, ProductVariantRequest variantRequest, String accessToken) {
        return restClient.post()
                .uri(ENDPOINT + "/products/" + productId + "/variants")
                .header("Authorization", "Bearer " + accessToken)
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
    }


    @Override
    public DataResponse<String> createVariantDetail(String variantId, List<ProductVariantDetailRequest> detailRequest, String accessToken) {
        return restClient.post()
                .uri(ENDPOINT + "/products/variants/" + variantId + "/details")
                .header("Authorization", "Bearer " + accessToken)
                .body(detailRequest)
                .exchange((request, response) -> {
                            System.out.println("createVariantDetail");
                            System.out.println(response.getStatusCode());
                            DataResponse<String> detailResponseDataResponse = null;

                            if (response.getBody().available() > 0) {
                                detailResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                                });
                            }
                            assert detailResponseDataResponse != null;
                            return detailResponseDataResponse;
                        }
                );
    }

    @Override
    public DataResponse<ProductResponse> updateProduct(String productId, ProductRequest productRequest, String accessToken) {
        return restClient.put()
                .uri(ENDPOINT + "/products/" + productId)
                .header("Authorization", "Bearer " + accessToken)
                .body(productRequest)
                .exchange((request, response) -> {
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());
                    DataResponse<ProductResponse> productResponseDataResponse = null;
                    if (response.getBody().available() > 0) {
                        productResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                        });
                    }
                    assert productResponseDataResponse != null;
                    return productResponseDataResponse;
                });
    }


    @Override
    public DataResponse<ProductVariantResponse> updateVariant(String variantId, ProductVariantRequest variantRequest, String accessToken) {
        return restClient.put()
                .uri(ENDPOINT + "/products/variants/" + variantId)
                .header("Authorization", "Bearer " + accessToken)
                .body(variantRequest)
                .exchange((request, response) -> {
                    System.out.println("updateVariant");
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());
                    DataResponse<ProductVariantResponse> variantResponseDataResponse = null;
                    if (response.getBody().available() > 0) {
                        variantResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                        });
                    }
                    assert variantResponseDataResponse != null;
                    return variantResponseDataResponse;
                });
    }


    @Override
    public DataResponse<Boolean> updateVariantDetail(String detailId, ProductDetailUpdateRequest detailRequest, String accessToken) {
        return restClient.put()
                .uri(ENDPOINT + "/products/variants/details/" + detailId)
                .header("Authorization", "Bearer " + accessToken)
                .body(detailRequest)
                .exchange((request, response) -> {
                    System.out.println("updateVariantDetail");
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());
                    DataResponse<Boolean> detailResponseDataResponse = null;
                    if (response.getBody().available() > 0) {
                        detailResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                        });
                    }
                    assert detailResponseDataResponse != null;
                    return detailResponseDataResponse;
                });
    }

    @Override
    public DataResponse<Object> deleteProduct(String productId, String accessToken) {
        return restClient.delete()
                .uri(ENDPOINT + "/products/" + productId)
                .header("Authorization", "Bearer " + accessToken)
                .exchange((request, response) -> {
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());
                    DataResponse<Object> deleteResponse = null;
                    if (response.getBody().available() > 0) {
                        deleteResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                        });
                    }
                    assert deleteResponse != null;
                    return deleteResponse;
                });
    }


    @Override
    public DataResponse<Object> deleteVariant(String variantId, String accessToken) {
        return restClient.delete()
                .uri(ENDPOINT + "/products/variants/" + variantId)
                .header("Authorization", "Bearer " + accessToken)
                .exchange((request, response) -> {
                    System.out.println("deleteVariant");
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());
                    DataResponse<Object> deleteResponse = null;
                    if (response.getBody().available() > 0) {
                        deleteResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                        });
                    }
                    assert deleteResponse != null;
                    return deleteResponse;
                });
    }


    @Override
    public DataResponse<Object> deleteVariantDetail(String detailId, String accessToken) {
        return restClient.delete()
                .uri(ENDPOINT + "/products/variants/details/" + detailId)
                .header("Authorization", "Bearer " + accessToken)
                .exchange((request, response) -> {
                    System.out.println("deleteVariantDetail");
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());
                    DataResponse<Object> deleteResponse = null;
                    if (response.getBody().available() > 0) {
                        deleteResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                        });
                    }
                    assert deleteResponse != null;
                    return deleteResponse;
                });
    }


    @Override
    public DataResponse<ProductResponse> getAllProducts(String accessToken) {
        return restClient.get()
                .uri(ENDPOINT + "/products")
                .header("Authorization", "Bearer " + accessToken)
                .exchange((request, response) -> {

                            System.out.println(response.getStatusCode());

                            DataResponse<ProductResponse> productResponseDataResponse = null;
                            if (response.getBody().available() > 0) {
                                productResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                                });
                            }
                            assert productResponseDataResponse != null;
                            return productResponseDataResponse;
                        }
                );
    }

    @Override
    public DataResponse<ProductResponse> getProductById(String productId) {
        return restClient.get()
                .uri(ENDPOINT + "/products/" + productId)
                .exchange((request, response) -> {
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());

                    DataResponse<ProductResponse> productResponseDataResponse = null;
                    if (response.getBody().available() > 0) {
                        productResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                        });
                    }
                    assert productResponseDataResponse != null;
                    return productResponseDataResponse;
                });
    }


    @Override
    public DataResponse<ProductVariantResponse> getVariantById(String variantId) {
        return restClient.get()
                .uri(ENDPOINT + "/products/variants/" + variantId)
                .exchange((request, response) -> {
                    System.out.println("getVariantById");
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());
                    DataResponse<ProductVariantResponse> variantResponseDataResponse = null;
                    if (response.getBody().available() > 0) {
                        variantResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                        });
                    }
                    assert variantResponseDataResponse != null;
                    return variantResponseDataResponse;
                });
    }


    @Override
    public DataResponse<ProductDetailResponse> getVariantDetailById(String detailId) {
        return restClient.get()
                .uri(ENDPOINT + "/products/variants/details/" + detailId)
                .exchange((request, response) -> {
                    System.out.println("getVariantDetailById");
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());
                    DataResponse<ProductDetailResponse> detailResponseDataResponse = null;
                    if (response.getBody().available() > 0) {
                        detailResponseDataResponse = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                        });
                    }
                    assert detailResponseDataResponse != null;
                    return detailResponseDataResponse;
                });
    }

    @Override
    public DataResponse<ProductVariantResponse> getAllProductVariantsByProductId(String productId) {
        return restClient.get()
                .uri(ENDPOINT + "/products/" + productId + "/variants")
                .exchange((request, response) -> {
                    System.out.println("getAllProductVariantsByProductId");
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());
                    DataResponse<ProductVariantResponse> variantResponses = null;
                    if (response.getBody().available() > 0) {
                        variantResponses = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                        });
                    }
                    assert variantResponses != null;
                    return variantResponses;
                });
    }

    @Override
    public DataResponse<ProductVariantDetailResponse> getAllVariantDetailsByVariantId(String variantId) {
        return restClient.get()
                .uri(ENDPOINT + "/products/variants/" + variantId + "/details")
                .exchange((request, response) -> {
                    System.out.println("getAllVariantDetailsByVariantId");
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());
                    DataResponse<ProductVariantDetailResponse> detailResponses = null;
                    if (response.getBody().available() > 0) {
                        detailResponses = objectMapper.readValue(response.getBody().readAllBytes(), new TypeReference<>() {
                        });
                    }
                    assert detailResponses != null;
                    return detailResponses;
                });
    }

    @Override
    public DataResponse<ProductVariantResponse> getAllVariants() {
        return restClient.get()
                .uri(ENDPOINT + "/products/variants/all")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public DataResponse<ProductVariantResponse> findProductVariantByProductVariantDetailId(String detailId) {
        return restClient.get()
                .uri(ENDPOINT + "/products/variants/productVariantDetail/" + detailId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public DataResponse<ProductDetailResponse> findProductVariantDetailByProductVariantAndColorAndMemory(String productVariantId, String color, String memory) {
        return restClient.get()
                .uri(ENDPOINT + "/products/variants/details/getProductVariantDetailByProductVariantAndColorAndMemory?" +
                        "productVariantId=" + productVariantId
                        + "&color=" + color
                        + "&memory=" + memory)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
