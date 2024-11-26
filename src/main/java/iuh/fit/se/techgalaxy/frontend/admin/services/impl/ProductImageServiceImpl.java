package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductsImageRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductsImageResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.ProductImgService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImgService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private static final String ENDPOINT = "http://localhost:8081";

    public ProductImageServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }
    @Override
    public DataResponse<ProductsImageResponse> createProductImg(String variantDetail, List<ProductsImageRequest> productsImageRequest,String accessToken) {
        return restClient.post()
                .uri(ENDPOINT + "/products/image/"+ variantDetail)
                .header("Authorization", "Bearer " + accessToken)
                .body(productsImageRequest)
                .exchange((request, response) -> {
                    System.out.println("Create product image");
                    System.out.println(response.getStatusCode());
                    DataResponse<ProductsImageResponse> productsImageResponseDataResponse = null;
                    if (response.getBody().available() > 0) {
                        productsImageResponseDataResponse = objectMapper.readValue(response.getBody(), DataResponse.class);
                    }
                    assert productsImageResponseDataResponse != null;
                    return productsImageResponseDataResponse;
                });
    }
}
