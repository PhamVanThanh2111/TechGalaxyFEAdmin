package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.AccountUpdateRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.AccountResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.AccountUpdateResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;

public interface AccountService {
    DataResponse<AccountResponse> findAll(String accessToken);

    DataResponse<AccountResponse> findById(String id, String accessToken);

    DataResponse<AccountResponse> findByEmail(String email, String accessToken);

    DataResponse<AccountUpdateResponse> update(AccountUpdateRequest accountUpdateRequest, String accessToken);

    DataResponse<AccountUpdateResponse> updateWithoutPassword(AccountUpdateRequest accountUpdateRequest, String accessToken);

    boolean existsByEmail(String email, String accessToken);

    DataResponse<Void> deleteAccount(String id, String accessToken);
}
