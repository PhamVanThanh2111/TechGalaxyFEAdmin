package iuh.fit.se.techgalaxy.frontend.admin.dto.request;

import java.util.List;

public class SetAtributeValueProductRequest {
    String productId;
    private List<AttributeValue> attributes;
    public static class AttributeValue {
        private String attributeId;
        private String value;
        // Getters và Setters
    }
}
//
//    {
//        "productId": "idProduct",
//            "attributes": [
//        {
//            "attributeId": "720",
//                "value": "Máy ảnh"
//        },
//        {
//            "attributeId": "manhinh",
//                "value": "6in"
//        },
//        {
//            "attributeId": "sim",
//                "value": "2sim"
//        }
//  ]
//    }

