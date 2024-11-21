package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Color;

import java.util.List;

public interface ColorService {
    public DataResponse<Color> getAllColors();
    public DataResponse<Color> getColorById(String id);
}
