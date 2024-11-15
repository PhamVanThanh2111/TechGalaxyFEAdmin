package iuh.fit.se.techgalaxy.frontend.admin.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsageCategoryResponse {
	String id;
	String name;
	String description;
	String avatar;
	String status;
	
}
