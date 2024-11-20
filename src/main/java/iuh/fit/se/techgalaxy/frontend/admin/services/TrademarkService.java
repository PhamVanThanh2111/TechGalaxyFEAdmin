package iuh.fit.se.techgalaxy.frontend.admin.services;


import iuh.fit.se.techgalaxy.frontend.admin.dto.request.TrademarkRequest;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.TrademarkResponse;

public interface TrademarkService {


    public DataResponse<TrademarkResponse> getTrademarkById(String id) ;

    public DataResponse<TrademarkResponse> getTrademarkByName(String name) ;

    public DataResponse<TrademarkResponse> getAllTrademarks();

    DataResponse<TrademarkResponse> findAll();

    DataResponse<TrademarkResponse> findById(String id);

    DataResponse<TrademarkResponse> save(String name);

    DataResponse<Object> delete(String id);

    DataResponse<TrademarkResponse> update(TrademarkRequest trademarkRequest);

}
