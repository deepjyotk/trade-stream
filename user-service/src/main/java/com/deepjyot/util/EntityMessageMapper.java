package com.deepjyot.util;

import com.deepjyot.entity.PortfolioItem;
import com.deepjyot.entity.User;
import com.deepjyot.user.Holding;
import com.deepjyot.user.StockTradeRequest;
import com.deepjyot.user.StockTradeResponse;
import com.deepjyot.user.UserInformation;

import java.util.List;

public class EntityMessageMapper {
    public static UserInformation toUserInformation(User user, List<PortfolioItem> items){
        var holdings = items.stream().map(i-> Holding.newBuilder().setTicker(i.getTicker()).setQuantity(i.getQuantity()).build()).toList();

        return UserInformation.newBuilder().setUserId(user.getId()).setName(user.getName()).setBalance(user.getBalance()).addAllHoldings(holdings).build();
    }

    public static PortfolioItem toPortfolioItem(StockTradeRequest request){
        var item = new PortfolioItem() ;
        item.setUserId(request.getUserId());
        item.setTicker(request.getTicker());
        item.setQuantity(request.getQuantity());

        return item ;
    }

    public static StockTradeResponse toStockTradeResponse(StockTradeRequest request, int balance) {
        return StockTradeResponse.newBuilder()
                .setUserId(request.getUserId())
                .setPrice(request.getPrice())
                .setTicker(request.getTicker())
                .setQuantity(request.getQuantity())
                .setAction(request.getAction())
                .setTotalPrice(request.getPrice() * request.getQuantity())
                .setBalance(balance)
                .build();
    }



}
