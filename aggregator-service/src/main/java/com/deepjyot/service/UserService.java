package com.deepjyot.service;


import com.deepjyot.user.UserInformation;
import com.deepjyot.user.UserInformationRequest;
import com.deepjyot.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub ;


    public UserInformation getUserInformation(int userId){
        var request = UserInformationRequest.newBuilder().setUserId(userId).build();

        return this.userServiceBlockingStub.getUserInformation(request) ;
    }
}
