package iuh.fit.se.techgalaxy.frontend.admin.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.CustomerRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.CustomerResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;

public interface CustomerService {
    DataResponse<CustomerResponse> findByEmail(String email);

    DataResponse<CustomerResponse> findAll();

    DataResponse<CustomerResponse> findById(String id);

    DataResponse<CustomerResponse> save(CustomerRequest customerRequest) throws JsonProcessingException;

    DataResponse<CustomerResponse> update(CustomerRequest customerRequest);

    DataResponse<Boolean> delete(String id);
}
