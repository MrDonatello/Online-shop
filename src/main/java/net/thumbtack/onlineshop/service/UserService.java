package net.thumbtack.onlineshop.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.daoImpl.UserDaoImpl;
import net.thumbtack.onlineshop.dto.request.LoginPasswordDto;
import net.thumbtack.onlineshop.dto.response.UserDtoResponse;
import net.thumbtack.onlineshop.dto.response.UserDtoResponseWithoutDeposit;
import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.exceptions.ApiError;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserDaoImpl userDao;
    private ObjectMapper objectMapper;
    private User user;
    private Client client;
    private UserDtoResponse userDtoResponse;
    private UserDtoResponseWithoutDeposit userDtoResponseWithoutDeposit;

    @Autowired
    public UserService(UserDaoImpl userDao, ObjectMapper objectMapper, User user, Client client, UserDtoResponse userDtoResponse, UserDtoResponseWithoutDeposit userDtoResponseWithoutDeposit) {
        this.userDao = userDao;
        this.objectMapper = objectMapper;
        this.user = user;
        this.client = client;
        this.userDtoResponse = userDtoResponse;
        this.userDtoResponseWithoutDeposit = userDtoResponseWithoutDeposit;
    }

    public UserDtoResponse login(LoginPasswordDto loginPasswordDto) throws ServiceException {
        user = userDao.login(loginPasswordDto.getLogin(), loginPasswordDto.getPassword());
        if (user.getRole().name().equals("ADMIN")) {
            userDtoResponse = objectMapper.convertValue(user, UserDtoResponse.class);
        }
        if (user.getRole().name().equals("CLIENT")) {
            userDtoResponseWithoutDeposit = objectMapper.convertValue(client = objectMapper.convertValue(user, Client.class), UserDtoResponseWithoutDeposit.class);
            userDtoResponse = objectMapper.convertValue(userDtoResponseWithoutDeposit, UserDtoResponse.class);
            userDtoResponse.setDeposit(String.valueOf(client.getDeposit().getDeposit()));
        }
        return userDtoResponse;
    }

    public UserDtoResponse infoAccounts(int userId) throws ServiceException {
        user = userDao.infoAccounts(userId);
        if (user.getRole().name().equals("ADMIN")) {
            userDtoResponse = objectMapper.convertValue(user, UserDtoResponse.class);
        }
        if (user.getRole().name().equals("CLIENT")) {
            client = objectMapper.convertValue(user, Client.class);
            int deposit = client.getDeposit().getDeposit();
            client.setDeposit(null);
            userDtoResponse = objectMapper.convertValue(client, UserDtoResponse.class);
            userDtoResponse.setDeposit(String.valueOf(deposit));
        }
        return userDtoResponse;
    }

    public void checkAdmin(int userId) throws ServiceException {
        if (!userDao.infoAccounts(userId).getRole().name().equals("ADMIN")) {
            List<ApiError> apiErrorList = new ArrayList<>();
            ApiError apiError = new ApiError();
            apiError.setErrorCode(ErrorCode.NO_ACCESS_PERMISSIONS.name());
            apiError.setMessage(ErrorCode.NO_ACCESS_PERMISSIONS.getErrorString());
            apiErrorList.add(apiError);
            throw new ServiceException(apiErrorList);
        }
    }

    public void checkClient(int userId) throws ServiceException {
        if (!userDao.infoAccounts(userId).getRole().name().equals("CLIENT")) {
            List<ApiError> apiErrorList = new ArrayList<>();
            ApiError apiError = new ApiError();
            apiError.setErrorCode(ErrorCode.USER_IS_A_NOT_CLIENT.name());
            apiError.setMessage(ErrorCode.USER_IS_A_NOT_CLIENT.getErrorString());
            apiErrorList.add(apiError);
            throw new ServiceException(apiErrorList);
        }
    }
}
