package com.deepjyot.service;

import com.deepjyot.stock.StockPriceRequest;
import com.deepjyot.stock.StockServiceGrpc;
import com.deepjyot.user.StockTradeRequest;
import com.deepjyot.user.StockTradeResponse;
import com.deepjyot.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class TradeService {
    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub ;


    @GrpcClient("stock-service")
    private StockServiceGrpc.StockServiceBlockingStub stockServiceBlockingStub ;


    public StockTradeResponse trade(StockTradeRequest stockTradeRequest){
        var priceRequest = StockPriceRequest.newBuilder().setTicker(stockTradeRequest.getTicker()).build();


        var priceResponse = this.stockServiceBlockingStub.getStockPrice(priceRequest) ;

        var tradeRequest = stockTradeRequest.toBuilder().setPrice(priceResponse.getPrice()).build() ;


        return this.userServiceBlockingStub.tradeStock(tradeRequest);
    }
}
