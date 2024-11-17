package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Memory;

import java.util.List;

public interface MemoryService {
    public DataResponse<Memory> getAllMemories();
    public DataResponse<Memory> getMemoryById(String id);
}
