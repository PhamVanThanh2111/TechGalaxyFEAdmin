package iuh.fit.se.techgalaxy.frontend.admin.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class DataResponse {
    private int status;
    private Object data;
    private String message;
    private Map<String, Object> errors;
}
