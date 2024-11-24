package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.AttributeResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ValueResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Attribute;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Value;

import java.util.List;

public interface AttributeService {
    public DataResponse<ValueResponse> getAttributesByVariantId(String variantId);
}
