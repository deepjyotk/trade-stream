package com.deepjyot.service;

import com.deepjyot.service.handler.StockTradeRequestHandler;
import com.deepjyot.service.handler.UserInformationRequestHandler;
import com.deepjyot.user.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    private final UserInformationRequestHandler userInformationRequestHandler;
    private final StockTradeRequestHandler stockTradeRequestHandler ;



    public UserService(UserInformationRequestHandler userInformationRequestHandler, StockTradeRequestHandler stockTradeRequestHandler) {
        this.userInformationRequestHandler = userInformationRequestHandler;
        this.stockTradeRequestHandler = stockTradeRequestHandler;
    }


    @Override
    public void getUserInformation(UserInformationRequest request, StreamObserver<UserInformation> responseObserver) {
        var userInformation = this.userInformationRequestHandler.getUserInformation(request) ;
        responseObserver.onNext(userInformation);
        responseObserver.onCompleted();
    }

    @Override
    public void tradeStock(StockTradeRequest request, StreamObserver<StockTradeResponse> responseObserver) {
        var response = TradeAction.SELL.equals(request.getAction()) ? this.stockTradeRequestHandler.sellStock(request):
                this.stockTradeRequestHandler.buyStock(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
