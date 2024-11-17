package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.TrademarkResponse;

public interface TrademarkService {

    public DataResponse<TrademarkResponse> getTrademarkById(String id) ;

    public DataResponse<TrademarkResponse> getTrademarkByName(String name) ;

    public DataResponse<TrademarkResponse> getAllTrademarks();
}
