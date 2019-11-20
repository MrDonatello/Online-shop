package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserDao {

    User login(String login, String password) throws ServiceException;

    User infoAccounts(int userId) throws ServiceException;

}
