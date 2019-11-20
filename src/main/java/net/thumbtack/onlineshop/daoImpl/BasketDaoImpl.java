package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.BasketDao;
import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.exceptions.ApiError;
import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.Deposit;
import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

@Service
public class BasketDaoImpl extends DaoImplBase implements BasketDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(Basket.class);

    public void addProductToBasket(Basket basket) throws ServiceException {
        List<ApiError> apiErrorList = new ArrayList<>();
        LOGGER.debug("DAO insert Basket");
        try (SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).addProductToBasket(basket.getProducts().get(0), basket.getClientId());
            } catch (RuntimeException e) {
                LOGGER.info("Can't insert Basket {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.ERROR_ADD_TO_DATABASE.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
    }

    public boolean isPresentProduct(Basket basket) throws ServiceException {
        List<ApiError> apiErrorList = new ArrayList<>();
        boolean bool = false;
        LOGGER.debug("DAO get Basket");
        try (SqlSession sqlSession = getSession()) {
            try {
                if (getBasketMapper(sqlSession).isHaveProduct(basket.getProducts().get(0).getId(), basket.getClientId()) != null) {
                    bool = true;
                }
            } catch (RuntimeException e) {
                LOGGER.info("Can't get Basket {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
        return bool;
    }

    public void updateBasket(Basket basket) throws ServiceException {
        List<ApiError> apiErrorList = new ArrayList<>();
        LOGGER.debug("DAO update Basket");
        try (SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).updateBasket(basket.getProducts().get(0).getId(), basket.getProducts().get(0).getCount(), basket.getClientId());
            } catch (RuntimeException e) {
                LOGGER.info("Can't update Basket {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
    }

    public void updateBasketCount(Basket basket) throws ServiceException {
        List<ApiError> apiErrorList = new ArrayList<>();
        LOGGER.debug("DAO update Basket");
        try (SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).updateBasketCount(basket.getProducts().get(0).getId(), basket.getProducts().get(0).getCount(), basket.getClientId());
            } catch (RuntimeException e) {
                LOGGER.info("Can't update Basket {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
    }


    public void deleteItemFromBasket(int clientId, int productId) throws ServiceException {
        List<ApiError> apiErrorList = new ArrayList<>();
        LOGGER.debug("DAO delete item from basket");
        try (SqlSession sqlSession = getSession()) {
            try {
                if (productId == 0) {
                    getBasketMapper(sqlSession).deleteRemovedProducts(clientId);
                } else {
                    getBasketMapper(sqlSession).deleteItemFromBasket(clientId, productId);
                }
            } catch (RuntimeException e) {
                LOGGER.info("Can't delete item from basket {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
    }

    public List<Product> getFullBasket(int clientId) throws ServiceException {
        List<ApiError> apiErrorList = new ArrayList<>();
        List<Product> basket;
        LOGGER.debug("DAO get full Basket");
        try (SqlSession sqlSession = getSession()) {
            try {
                basket = getBasketMapper(sqlSession).getFullBasket(clientId);
            } catch (RuntimeException e) {
                LOGGER.info("Can't get full Basket {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
        return basket;
    }

    public void purchasesFromBasket(Basket basket, Deposit deposit) throws ServiceException {
        List<ApiError> apiErrorList = new ArrayList<>();
        LockRegistry lockProduct = new DefaultLockRegistry();
        LOGGER.debug("DAO update Basket");
        try (SqlSession sqlSession = getSession()) {
            try {
                for (Product product : basket.getProducts()) {
                    Lock lockP = lockProduct.obtain(product.getId());
                    lockP.lock();
                    try {
                        if (getProductMapper(sqlSession).purchase(product) != 1) {
                            sqlSession.rollback();
                            List<ApiError> errorList = new ArrayList<>();
                            ApiError error = new ApiError(ErrorCode.INVALID_PURCHASE.name(), null, ErrorCode.INVALID_PURCHASE.getErrorString());
                            errorList.add(error);
                            throw new ServiceException(errorList);
                        }
                        getProductMapper(sqlSession).addPurchases(product, basket.getClientId());
                        getBasketMapper(sqlSession).deleteItemFromBasket(basket.getClientId(), product.getId());
                    } finally {
                        lockP.unlock();
                    }
                }
                if (getClientMapper(sqlSession).depositUpdate(deposit) != 1) {
                    sqlSession.rollback();
                    List<ApiError> errorList = new ArrayList<>();
                    ApiError error = new ApiError(ErrorCode.INVALID_DEPOSIT.name(), null, ErrorCode.INVALID_DEPOSIT.getErrorString());
                    errorList.add(error);
                    throw new ServiceException(errorList);
                }
            } catch (RuntimeException e) {
                LOGGER.info("Can't update Basket {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
    }
}
