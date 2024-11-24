package iuh.fit.se.techgalaxy.frontend.admin.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class AttributeValueRequest {
	
    String value;
    String attributeId;
    
}
