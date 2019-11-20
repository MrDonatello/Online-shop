package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Deposit;
import net.thumbtack.onlineshop.model.Product;
import org.springframework.stereotype.Service;

@Service
public interface ClientDao {

    Client insert(Client client) throws ServiceException;

    Client editClientProfile(Client client) throws ServiceException;

    void depositingMoney(Deposit deposit) throws ServiceException;

    Client getClientById(int id) throws ServiceException;

    Deposit getDeposit(int clientId) throws ServiceException;

    void purchase(Product purchase, Deposit deposit) throws ServiceException;
}
