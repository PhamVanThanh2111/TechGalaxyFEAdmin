package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.AccountUpdateRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.AccountResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.AccountUpdateResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;

public interface AccountService {
    DataResponse<AccountResponse> findAll();

    DataResponse<AccountResponse> findById(String id);

    DataResponse<AccountUpdateResponse> update(AccountUpdateRequest accountUpdateRequest);
}
