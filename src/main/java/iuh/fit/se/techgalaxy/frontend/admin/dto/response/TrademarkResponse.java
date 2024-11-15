package iuh.fit.se.techgalaxy.frontend.admin.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrademarkResponse {
    String id;
    String name;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
