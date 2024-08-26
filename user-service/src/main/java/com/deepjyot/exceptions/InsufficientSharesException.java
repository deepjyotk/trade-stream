package com.deepjyot.exceptions;

public class InsufficientSharesException extends  RuntimeException{

    private static final String MESSAGE = "User [id= %d] does not have enough shares to compelte the transaction" ;

    public InsufficientSharesException(Integer userId){
        super(MESSAGE.formatted(userId) ) ;
    }
}
