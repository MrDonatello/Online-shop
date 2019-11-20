package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductDao {

    Product addProduct(Product product) throws ServiceException;

    void addProductCategory(List<Integer> categoriesId, int productId) throws ServiceException;

    void removeProductCategory(int productId) throws ServiceException;

    void editProductName(Product product) throws ServiceException;

    void editProductPrice(Product product) throws ServiceException;

    void editProductCount(Product product) throws ServiceException;

    void deleteProduct(int id) throws ServiceException;

    Product checkProductById(String id) throws ServiceException;

    List<Product> getProductWithOutCategory() throws ServiceException;

    List<Product> getAllProduct() throws ServiceException;

    List<Product> getProductByCategory(String id) throws ServiceException;

    List<Integer> getCategoryById(int productId) throws ServiceException;

}


