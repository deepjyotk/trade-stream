package com.deepjyot.service.handler;

import com.deepjyot.common.Ticker;
import com.deepjyot.exceptions.InsufficientBalanceException;
import com.deepjyot.exceptions.InsufficientSharesException;
import com.deepjyot.exceptions.UnknownTickerException;
import com.deepjyot.exceptions.UnknownUserException;
import com.deepjyot.repository.PortfolioItemRepository;
import com.deepjyot.repository.UserRepository;
import com.deepjyot.user.StockTradeRequest;
import com.deepjyot.user.StockTradeResponse;
import com.deepjyot.util.EntityMessageMapper;
import io.grpc.InternalGlobalInterceptors;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StockTradeRequestHandler {

    private final UserRepository userRepository ;
    private  final PortfolioItemRepository portfolioItemRepository ;

    public StockTradeRequestHandler(UserRepository userRepository, PortfolioItemRepository portfolioItemRepository){
        this.userRepository = userRepository ;
        this.portfolioItemRepository = portfolioItemRepository ;
    }

    //it means that since in this service, we are updating 2 tables in this service, so it should be
    //both the tables should update; if only 1 table updates and the next fails.. it should rollback.
    @Transactional
    public StockTradeResponse buyStock(StockTradeRequest request){
        //validate - valid ticker, valid user, and enough balance.

        //ticker validate.
        this.validateTicker(request.getTicker());


        //get the valid user.
        var user = this.userRepository.findById(request.getUserId()).orElseThrow( ()-> new UnknownUserException(request.getUserId()));

        //user should have enough balance
        var totalPrice = request.getQuantity() * request.getPrice() ;
        this.validateUserBalance(user.getId(), user.getBalance(), totalPrice);

        //now all the validations are done.

        //updating the user balance -> User table update
        user.setBalance(user.getBalance() -totalPrice );

        // updating the Portfolio Item table: which maps user Id and ticker.
        this.portfolioItemRepository.findByUserIdAndTicker(user.getId(),request.getTicker())
                .ifPresentOrElse(
                        portfolioItem -> portfolioItem.setQuantity(portfolioItem.getQuantity()+request.getQuantity()),
                        ()-> this.portfolioItemRepository.save(EntityMessageMapper.toPortfolioItem(request))
                );



        return EntityMessageMapper.toStockTradeResponse(request,user.getBalance()) ;

    }

    //it means that since in this service, we are updating 2 tables in this service, so it should be
    //both the tables should update; if only 1 table updates and the next fails.. it should rollback.
    @Transactional
    public StockTradeResponse sellStock(StockTradeRequest request){
        //validate - valid ticker, valid user, and enough balance.

        //ticker validate.
        this.validateTicker(request.getTicker());


        //get the valid user.
        var user = this.userRepository.findById(request.getUserId()).orElseThrow( ()-> new UnknownUserException(request.getUserId()));


        var portfolioItem = this.portfolioItemRepository.findByUserIdAndTicker(user.getId(),request.getTicker()).filter(
                pi -> pi.getQuantity() >= request.getQuantity())
                .orElseThrow(()-> new InsufficientSharesException(user.getId()));

        //valid request.
        //user should have enough balance
        var totalPrice = request.getQuantity() * request.getPrice() ;
        user.setBalance(user.getBalance() + totalPrice);

        portfolioItem.setQuantity(portfolioItem.getQuantity()-request.getQuantity());


        return EntityMessageMapper.toStockTradeResponse(request,user.getBalance()) ;
    }


    private void validateTicker(Ticker ticker){
        if(Ticker.UNKNOWN.equals(ticker)){
            throw new UnknownTickerException();
        }
    }

    private void validateUserBalance(Integer userId,Integer userBalance, Integer totalPrice){
        if(totalPrice>userBalance){
            throw  new InsufficientBalanceException(userId);
        }
    }
}
