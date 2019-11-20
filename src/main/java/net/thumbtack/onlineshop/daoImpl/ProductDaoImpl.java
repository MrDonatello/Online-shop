package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.ProductDao;
import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.exceptions.ApiError;
import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDaoImpl extends DaoImplBase implements ProductDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(Product.class);

    public Product addProduct(Product product) throws ServiceException {
        List<ApiError> apiErrorList = new ArrayList<>();
        LOGGER.debug("DAO insert Product {}", product);
        try (SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).addProduct(product);
            } catch (RuntimeException e) {
                LOGGER.info("Can't insert Product {}, {}", product, e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.ERROR_ADD_TO_DATABASE.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
        return product;
    }

    public void addProductCategory(List<Integer> categoriesId, int productId) throws ServiceException {
        List<ApiError> apiErrorList = new ArrayList<>();
        LOGGER.debug("DAO insert Product category {}");
        try (SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).addProductCategory(categoriesId, productId);
            } catch (RuntimeException e) {
                LOGGER.info("Can't insert Product category {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.ERROR_ADD_TO_DATABASE.name(), "categories", e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
    }

    public void removeProductCategory(int productId) throws ServiceException {
        List<ApiError> apiErrorList = new ArrayList<>();
        LOGGER.debug("DAO remove ProductCategory {}");
        try (SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).removeProductCategory(productId);
            } catch (RuntimeException e) {
                LOGGER.info("Can't remove ProductCategory {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
    }

    public void editProductName(Product product) throws ServiceException {
        LOGGER.debug("DAO edit Product {}");
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).editProductName(product);
            } catch (RuntimeException e) {
                LOGGER.info("Can't edit Product {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), "name", e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
    }

    public void editProductPrice(Product product) throws ServiceException {
        LOGGER.debug("DAO edit Product {}");
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).editProductPrice(product);
            } catch (RuntimeException e) {
                LOGGER.info("Can't edit Product {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), "price", e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
    }

    public void editProductCount(Product product) throws ServiceException {
        LOGGER.debug("DAO edit Product {}");
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).editProductCount(product);
            } catch (RuntimeException e) {
                LOGGER.info("Can't edit Product {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), "count", e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
    }

    public void deleteProduct(int id) throws ServiceException {
        LOGGER.debug("DAO delete Product {}");
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).deleteProduct(id);
                getProductMapper(sqlSession).removeProductCategory(id);
            } catch (RuntimeException e) {
                LOGGER.info("Can't delete Product {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
    }

    public Product checkProductById(String id) throws ServiceException {
        LOGGER.debug("DAO check Product {}");
        Product product;
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                product = getProductMapper(sqlSession).checkProductById(id);
                if (product == null) {
                    sqlSession.rollback();
                    ApiError apiError = new ApiError(ErrorCode.PRODUCT_DOES_NOT_EXIST.name(), "id", ErrorCode.PRODUCT_DOES_NOT_EXIST.getErrorString());
                    apiErrorList.add(apiError);
                    throw new ServiceException(apiErrorList);
                }
            } catch (RuntimeException e) {
                LOGGER.info("Can't check Product {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), "id", e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
        return product;
    }

    public List<Product> getProductWithOutCategory() throws ServiceException {
        LOGGER.debug("DAO get Product {}");
        List<Product> products;
        try (SqlSession sqlSession = getSession()) {
            try {
                products = getProductMapper(sqlSession).getProductWithOutCategory();
            } catch (RuntimeException e) {
                LOGGER.info("Can't get Product {}", e);
                sqlSession.rollback();
                List<ApiError> errorList = new ArrayList<>();
                ApiError error = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                errorList.add(error);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return products;
    }

    public List<Product> getAllProduct() throws ServiceException {
        LOGGER.debug("DAO get Product {}");
        List<Product> products;
        try (SqlSession sqlSession = getSession()) {
            try {
                products = getProductMapper(sqlSession).getAllProduct();
            } catch (RuntimeException e) {
                LOGGER.info("Can't get Product {}", e);
                sqlSession.rollback();
                List<ApiError> errorList = new ArrayList<>();
                ApiError error = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                errorList.add(error);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return products;
    }

    public List<Product> getProductByCategory(String id) throws ServiceException {
        LOGGER.debug("DAO get Product {}");
        List<Product> products;
        try (SqlSession sqlSession = getSession()) {
            try {
                products = getProductMapper(sqlSession).getProductByCategory(id);
            } catch (RuntimeException e) {
                LOGGER.info("Can't get Product {}", e);
                sqlSession.rollback();
                List<ApiError> errorList = new ArrayList<>();
                ApiError error = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                errorList.add(error);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return products;
    }

    public List<Integer> getCategoryById(int productId) throws ServiceException {
        LOGGER.debug("DAO get Product {}");
        List<Integer> id;
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                id = getProductMapper(sqlSession).getCategoryById(productId);
            } catch (RuntimeException e) {
                LOGGER.info("Can't get Product {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), "id", e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
        return id;
    }
}
