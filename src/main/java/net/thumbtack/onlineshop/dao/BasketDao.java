package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.Deposit;
import net.thumbtack.onlineshop.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BasketDao {

    void addProductToBasket(Basket basket) throws ServiceException;

    boolean isPresentProduct(Basket basket) throws ServiceException;

    void updateBasket(Basket basket) throws ServiceException;

    void updateBasketCount(Basket basket) throws ServiceException;

    void deleteItemFromBasket(int clientId, int productId) throws ServiceException;

    List<Product> getFullBasket(int clientId) throws ServiceException;

    void purchasesFromBasket(Basket basket, Deposit deposit) throws ServiceException;

}
