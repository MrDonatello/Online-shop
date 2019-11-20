package net.thumbtack.onlineshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.daoImpl.ClientDaoImpl;
import net.thumbtack.onlineshop.dto.request.ClientDto;
import net.thumbtack.onlineshop.dto.request.ClientDtoWithoutLogin;
import net.thumbtack.onlineshop.dto.request.DepositDto;
import net.thumbtack.onlineshop.dto.response.UserDtoResponse;
import net.thumbtack.onlineshop.dto.response.UserDtoResponseWithoutDeposit;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Deposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientDaoImpl clientDao;
    private ObjectMapper objectMapper;

    @Autowired
    public ClientService(ClientDaoImpl clientDao, ObjectMapper objectMapper) {
        this.clientDao = clientDao;
        this.objectMapper = objectMapper;
    }

    public UserDtoResponse insert(ClientDto clientDto) throws ServiceException {
        Client client = objectMapper.convertValue(clientDto, Client.class);
        UserDtoResponse userDtoResponse = objectMapper.convertValue(clientDao.insert(client), UserDtoResponse.class);
        userDtoResponse.setDeposit(String.valueOf(clientDao.getDeposit(client.getId()).getDeposit()));
        return userDtoResponse;
    }

    public UserDtoResponse editClientProfile(ClientDtoWithoutLogin clientDto) throws ServiceException {
        Client client = (objectMapper.convertValue(clientDto, Client.class));
        client.setPassword(clientDto.getNewPassword());
        UserDtoResponse userDtoResponse = objectMapper.convertValue(clientDao.editClientProfile(client), UserDtoResponse.class);
        userDtoResponse.setDeposit(String.valueOf(clientDao.getDeposit(client.getId()).getDeposit()));
        return userDtoResponse;
    }

    public UserDtoResponse depositingMoney(DepositDto depositDto) throws ServiceException {
    	// REVU что-то здесь не так
    	// На входе есть clientId. По нему нало получить Client, а в нем будет и его Deposit
    	// у Вас этот метод есть, но Вы зачем-то начали делать обходным путем
    	// не нужен тут вообще никакой new Deposit(). С какой стати вообщк новому депозиту создаваться ?
        Deposit deposit = new Deposit();
        deposit.setDeposit(Integer.parseInt(depositDto.getDeposit()));
        deposit.setClientId(depositDto.getClientId());
        Client client = clientDao.getClientById(deposit.getClientId());
        deposit.setDeposit(deposit.getDeposit() + client.getDeposit().getDeposit());
        deposit.setVersion(client.getDeposit().getVersion());
        clientDao.depositingMoney(deposit);
        UserDtoResponseWithoutDeposit responseWithoutDeposit = objectMapper.convertValue(client, UserDtoResponseWithoutDeposit.class);
        UserDtoResponse userDtoResponse = objectMapper.convertValue(responseWithoutDeposit, UserDtoResponse.class);
        userDtoResponse.setDeposit(String.valueOf(deposit.getDeposit()));
        return userDtoResponse;
    }

    public UserDtoResponse getDeposit(int id) throws ServiceException {
        Client client = clientDao.getClientById(id);
        UserDtoResponseWithoutDeposit responseWithoutDeposit = objectMapper.convertValue(client, UserDtoResponseWithoutDeposit.class);
        UserDtoResponse userDtoResponse = objectMapper.convertValue(responseWithoutDeposit, UserDtoResponse.class);
        userDtoResponse.setDeposit(String.valueOf(client.getDeposit().getDeposit()));
        return userDtoResponse;
    }
}
