package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryDao {

    Category addCategory(Category category) throws ServiceException;

    Category getCategory(String categoryId) throws ServiceException;

    String getCategoryName(int categoryId) throws ServiceException;

    Category editCategoryParent(Category category) throws ServiceException;

    Category getParentById(Category category) throws ServiceException;

    void deleteCategory(String id) throws ServiceException;

    Category editCategoryName(Category category) throws ServiceException;

    List<Category> getAllCategory() throws ServiceException;
}
