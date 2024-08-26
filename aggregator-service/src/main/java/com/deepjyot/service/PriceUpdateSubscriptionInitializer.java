package com.deepjyot.service;

import com.deepjyot.stock.StockServiceGrpc;
import com.google.protobuf.Empty;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

// this is a spring service, which will work like when the application starts, it will automatically connect to
// stock-service, and get the price updates....which will be handled by priceUpdateListener.
@Service
public class PriceUpdateSubscriptionInitializer implements CommandLineRunner {


    @GrpcClient("stock-service")
    private StockServiceGrpc.StockServiceStub stockServiceStub ;

    @Autowired
    PriceUpdateListener priceUpdateListener ;

    @Override
    public void run(String... args) throws Exception {
        this.stockServiceStub
                .withWaitForReady()
        .getPriceUpdates(Empty.getDefaultInstance(),priceUpdateListener);
    }
}
