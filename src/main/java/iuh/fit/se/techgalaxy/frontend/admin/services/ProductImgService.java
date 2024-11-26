package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductsImageRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductsImageResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ProductImgService {
    public DataResponse<ProductsImageResponse> createProductImg(String variantDetail, List<ProductsImageRequest> productsImageRequest, String accessToken);
}
