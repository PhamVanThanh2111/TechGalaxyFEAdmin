package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.AccountResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;

public interface AccountService {
    DataResponse<AccountResponse> findAll();
}
