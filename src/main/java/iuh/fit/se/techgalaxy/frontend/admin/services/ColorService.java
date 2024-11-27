package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Color;

public interface ColorService {
    DataResponse<Color> getAllColors();

    DataResponse<Color> getColorById(String id);
}
