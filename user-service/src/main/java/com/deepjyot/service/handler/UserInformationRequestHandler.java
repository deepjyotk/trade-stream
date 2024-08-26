package com.deepjyot.service.handler;

import com.deepjyot.exceptions.UnknownUserException;
import com.deepjyot.repository.PortfolioItemRepository;
import com.deepjyot.repository.UserRepository;
import com.deepjyot.user.UserInformation;
import com.deepjyot.user.UserInformationRequest;
import com.deepjyot.util.EntityMessageMapper;
import org.springframework.stereotype.Service;

@Service
public class UserInformationRequestHandler {
    private final UserRepository userRepository ;
    private final PortfolioItemRepository portfolioItemRepository ;

    public UserInformationRequestHandler(UserRepository userRepository, PortfolioItemRepository portfolioItemRepository) {
        this.userRepository = userRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    public UserInformation getUserInformation(UserInformationRequest request) {
        var user = this.userRepository.findById(request.getUserId()).orElseThrow( ()-> new UnknownUserException(request.getUserId()));
        var portfolioItem = this.portfolioItemRepository.findAllByUserId(request.getUserId()) ;

        return EntityMessageMapper.toUserInformation(user, portfolioItem) ;
    }
}
