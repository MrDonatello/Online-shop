package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.CategoryDao;
import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.exceptions.ApiError;
import net.thumbtack.onlineshop.model.Category;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryDaoImpl extends DaoImplBase implements CategoryDao {


    private static final Logger LOGGER = LoggerFactory.getLogger(Category.class);

    public Category addCategory(Category category) throws ServiceException {
        LOGGER.debug("DAO insert Category {}", category);
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).addCategory(category);
            } catch (RuntimeException e) {
                LOGGER.info("Can't insert Category {}, {}", category, e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.ERROR_ADD_TO_DATABASE.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
        return category;
    }

    public Category getCategory(String categoryId) throws ServiceException {
        LOGGER.debug("DAO get Category");
        Category category;
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                category = getCategoryMapper(sqlSession).getCategory(categoryId);
                if (category == null) {
                    sqlSession.rollback();
                    List<ApiError> errorList = new ArrayList<>();
                    ApiError error = new ApiError(ErrorCode.CATEGORY_DOES_NOT_EXIST.name(), "id ", ErrorCode.CATEGORY_DOES_NOT_EXIST.getErrorString());
                    errorList.add(error);
                    throw new ServiceException(errorList);
                }
            } catch (RuntimeException e) {
                LOGGER.info("Can't get Category {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.ERROR_ADD_TO_DATABASE.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
        return category;
    }

    public String getCategoryName(int categoryId) throws ServiceException {
        LOGGER.debug("DAO get Category");
        String category;
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                category = getCategoryMapper(sqlSession).getCategoryName(categoryId);
            } catch (RuntimeException e) {
                LOGGER.info("Can't get Category {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
        return category;
    }

    public Category editCategoryParent(Category category) throws ServiceException {
        LOGGER.debug("DAO edit Category");
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).updateParentCategory(category);
                category.setParent(getCategoryMapper(sqlSession).getParent(category));
            } catch (RuntimeException e) {
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
        return category;
    }

    public Category getParentById(Category category) throws ServiceException {
        LOGGER.debug("DAO edit Category");
        Category categoryParent;
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                categoryParent = getCategoryMapper(sqlSession).getParentById(category);
            } catch (RuntimeException e) {
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
        return categoryParent;
    }

    public void deleteCategory(String id) throws ServiceException {
        LOGGER.debug("DAO delete Category");
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).deleteCategory(id);
                getCategoryMapper(sqlSession).deleteSubCategory(id);
            } catch (RuntimeException e) {
                LOGGER.info("Can't delete Category {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
    }

    public Category editCategoryName(Category category) throws ServiceException {
        LOGGER.debug("DAO edit Category");
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).updateNameCategory(category);
            } catch (RuntimeException e) {
                LOGGER.info("Can't edit Category {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
        return category;
    }

    public List<Category> getAllCategory() throws ServiceException {
        LOGGER.debug("DAO getAll Category");
        List<Category> categories;
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                categories = getCategoryMapper(sqlSession).getAllCategory();
            } catch (RuntimeException e) {
                LOGGER.info("Can't getAll Category {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
        return categories;
    }

}
