package com.deepjyot.aggregator.tests.mockservice;

import com.deepjyot.common.Ticker;
import com.deepjyot.stock.PriceUpdate;
import com.deepjyot.stock.StockPriceRequest;
import com.deepjyot.stock.StockPriceResponse;
import com.deepjyot.stock.StockServiceGrpc;
import com.deepjyot.user.StockTradeResponse;
import com.google.common.util.concurrent.Uninterruptibles;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class StockMockService extends StockServiceGrpc.StockServiceImplBase {
    private static  final Logger log = LoggerFactory.getLogger(StockMockService.class) ;


    @Override
    public void getPriceUpdates(Empty request, StreamObserver<PriceUpdate> responseObserver) {
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        for(int i=1;i<=5;i++){
            var priceUpdateObject = PriceUpdate.newBuilder().setPrice(i).setTicker(Ticker.AMAZON).build();
            log.info("{}", priceUpdateObject);

            responseObserver.onNext(priceUpdateObject);
        }

        responseObserver.onCompleted();
    }

    @Override
    public void getStockPrice(StockPriceRequest request, StreamObserver<StockPriceResponse> responseObserver) {
        var stockPriceResponse = StockPriceResponse.newBuilder().setPrice(15).build();

        responseObserver.onNext(stockPriceResponse);
        responseObserver.onCompleted();
    }
}
