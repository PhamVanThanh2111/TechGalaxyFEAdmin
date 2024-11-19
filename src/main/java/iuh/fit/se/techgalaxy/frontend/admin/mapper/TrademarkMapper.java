package iuh.fit.se.techgalaxy.frontend.admin.mapper;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.TrademarkRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.TrademarkResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Trademark;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrademarkMapper {
    TrademarkMapper INSTANCE = Mappers.getMapper(TrademarkMapper.class);

    TrademarkRequest toTrademarkRequest(Trademark trademark);

    TrademarkResponse toTrademarkResponse(Trademark trademark);

    Trademark toTrademarkFromRequest(TrademarkRequest trademarkRequest);

    Trademark toTrademarkFromResponse(TrademarkResponse trademarkResponse);
}
