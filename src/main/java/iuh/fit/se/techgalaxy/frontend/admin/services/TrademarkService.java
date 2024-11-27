package iuh.fit.se.techgalaxy.frontend.admin.services;


import iuh.fit.se.techgalaxy.frontend.admin.dto.request.TrademarkRequest;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.TrademarkResponse;

public interface TrademarkService {


    DataResponse<TrademarkResponse> getTrademarkById(String id);

    DataResponse<TrademarkResponse> getTrademarkByName(String name);

    DataResponse<TrademarkResponse> getAllTrademarks();

    DataResponse<TrademarkResponse> findAll();

    DataResponse<TrademarkResponse> findById(String id);

    DataResponse<TrademarkResponse> save(String name, String accessToken);

    DataResponse<Object> delete(String id, String accessToken);

    DataResponse<TrademarkResponse> update(TrademarkRequest trademarkRequest, String accessToken);

}
