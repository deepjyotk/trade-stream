package com.deepjyot.user.tests;

import com.deepjyot.UserServiceApplication;
import com.deepjyot.common.Ticker;
import com.deepjyot.user.StockTradeRequest;
import com.deepjyot.user.TradeAction;
import com.deepjyot.user.UserInformationRequest;
import com.deepjyot.user.UserServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "grpc.server.port=-1",
        "grpc.server.in-process-name=integration-test",
        "grpc.client.user-service.address=in-process:integration-test"
})
public class UserServiceTest {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub stub ;

    @Test
    public void userInformationTest(){
        var request = UserInformationRequest.newBuilder().setUserId(1).build() ;
        var response  = this.stub.getUserInformation(request) ;


        Assertions.assertEquals(10000, response.getBalance());
        Assertions.assertEquals("Sam", response.getName());

        Assertions.assertTrue(response.getHoldingsList().isEmpty());
    }

    @Test
    public void unknownUserTest(){

        var ex = Assertions.assertThrows(StatusRuntimeException.class , ()->{
            var request = UserInformationRequest.newBuilder().setUserId(-1).build() ;
            var response  = this.stub.getUserInformation(request) ;
        });

        Assertions.assertEquals(Status.Code.NOT_FOUND,ex.getStatus().getCode());



//        Assertions.assertEquals(10000, response.getBalance());
//        Assertions.assertEquals("Sam", response.getName());
//
//        Assertions.assertTrue(response.getHoldingsList().isEmpty());
    }

    @Test
    public void unknownTickerBuyTest(){

        var ex = Assertions.assertThrows(StatusRuntimeException.class , ()->{

            // no ticker provided
            var request = StockTradeRequest.newBuilder().setUserId(1)
                    .setPrice(1).setQuantity(1).setAction(TradeAction.BUY).build();


            var response  = this.stub.tradeStock(request) ;
        });

        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT,ex.getStatus().getCode());

//        Assertions.assertEquals(10000, response.getBalance());
//        Assertions.assertEquals("Sam", response.getName());
//
//        Assertions.assertTrue(response.getHoldingsList().isEmpty());
    }


    @Test
    public void insufficientShareTest(){

        var ex = Assertions.assertThrows(StatusRuntimeException.class , ()->{

            // no ticker provided
            var request = StockTradeRequest.newBuilder().setUserId(1)
                    .setPrice(1).setQuantity(100002).setTicker(Ticker.AMAZON)
                    .setAction(TradeAction.BUY).build();


            var response  = this.stub.tradeStock(request) ;
        });

        Assertions.assertEquals(Status.Code.FAILED_PRECONDITION,ex.getStatus().getCode());
    }



    @Test
    public void insufficientBalanceTest(){

        var ex = Assertions.assertThrows(StatusRuntimeException.class , ()->{
            var request = StockTradeRequest.newBuilder().setUserId(1)
                    .setPrice(1).setQuantity(10001).setTicker(Ticker.AMAZON)
                    .setAction(TradeAction.BUY).build();


            var response  = this.stub.tradeStock(request) ;
        });

        Assertions.assertEquals(Status.Code.FAILED_PRECONDITION,ex.getStatus().getCode());
    }


    @Test
    public void buySellTest(){



        var request = StockTradeRequest.newBuilder().setUserId(2)
                .setPrice(100).setQuantity(5).setTicker(Ticker.AMAZON)
                .setAction(TradeAction.BUY).build();


        var response  = this.stub.tradeStock(request) ;

        Assertions.assertEquals(9500, response.getBalance());


        var userReq = UserInformationRequest.newBuilder().setUserId(2).build() ;
        var userResponse = this.stub.getUserInformation(userReq) ;

        Assertions.assertEquals(1,userResponse.getHoldingsCount());

        Assertions.assertEquals(Ticker.AMAZON, userResponse.getHoldingsList().getFirst().getTicker());


        //sell:

        var sellReq = request.toBuilder().setAction(TradeAction.SELL).setPrice(102).build() ;
        var sellResponse = this.stub.tradeStock(sellReq) ;


        Assertions.assertEquals(10010, sellResponse.getBalance());
    }









}
