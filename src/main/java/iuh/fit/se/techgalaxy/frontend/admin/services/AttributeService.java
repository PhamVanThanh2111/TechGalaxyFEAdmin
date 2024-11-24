package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.AttributeValueRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.AttributeValueUpdateRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.AttributeResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ValueResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Attribute;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Value;

import java.util.List;

public interface AttributeService {
    DataResponse<ValueResponse> getAttributesByVariantId(String variantId);

    DataResponse<AttributeResponse> getAllAttribute();

    DataResponse<AttributeResponse> getAttributeById(String id);


    DataResponse<Object> createAttributeValueByVariantId(String variantId, List<AttributeValueRequest> values);

    DataResponse<ValueResponse> getValueByNameAtribute(String name);

    DataResponse<ValueResponse> updateValueProductVariant(String variantId, AttributeValueUpdateRequest attributeValueRequest);

    DataResponse<ValueResponse> deleteValue(String valueId);

    DataResponse<ValueResponse> getValueById(String valueId);
}
