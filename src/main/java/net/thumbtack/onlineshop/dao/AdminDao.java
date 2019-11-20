package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.model.Admin;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Purchase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AdminDao {

    Admin insert(Admin admin) throws ServiceException;

    Admin editAdminProfile(Admin admin) throws ServiceException;

    List<Client> getClientInfo() throws ServiceException;

     List<Purchase> getReport(Map<String, String> map) throws ServiceException;
}
