package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.UsageCategoryRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductVariantResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.UsageCategoryResponse;

import java.util.List;

public interface UsageCategoryService {
    DataResponse<UsageCategoryResponse> getAllUsageCategories();

    DataResponse<UsageCategoryResponse> getUsageCategoryById(String id);

}
