package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.TrademarkRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.TrademarkResponse;

public interface TrademarkService {
    DataResponse<TrademarkResponse> findAll();

    DataResponse<TrademarkResponse> findById(String id);

    DataResponse<TrademarkResponse> save(String name);

    DataResponse<Object> delete(String id);

    DataResponse<TrademarkResponse> update(TrademarkRequest trademarkRequest);
}
