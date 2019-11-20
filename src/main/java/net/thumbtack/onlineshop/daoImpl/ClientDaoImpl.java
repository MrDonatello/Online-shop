package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.ClientDao;
import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.exceptions.ApiError;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Deposit;
import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientDaoImpl extends DaoImplBase implements ClientDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    public Client insert(Client client) throws ServiceException {
        LOGGER.debug("DAO insert Client {}", client);
        try (SqlSession sqlSession = getSession()) {
            try {
                getClientMapper(sqlSession).insertUser(client);
                getClientMapper(sqlSession).insertClient(client);
                getClientMapper(sqlSession).addDeposit(client);
            } catch (RuntimeException e) {
                LOGGER.info("Can't insert Client {}, {}", client, e);
                sqlSession.rollback();
                List<ApiError> errorList = new ArrayList<>();
                ApiError error = new ApiError(ErrorCode.ERROR_ADD_TO_DATABASE.name(), null, e.getMessage());
                errorList.add(error);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return client;
    }

    public Client editClientProfile(Client client) throws ServiceException {
        LOGGER.debug("DAO Edit Client {}", client);
        try (SqlSession sqlSession = getSession()) {
            try {
                getClientMapper(sqlSession).EditProfileClient(client);
            } catch (RuntimeException e) {
                LOGGER.info("Can't Edit Client {}, {}", client, e);
                sqlSession.rollback();
                List<ApiError> errorList = new ArrayList<>();
                ApiError error = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                errorList.add(error);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return client;
    }

    public void depositingMoney(Deposit deposit) throws ServiceException {
        LOGGER.debug("DAO depositingMoney");
        try (SqlSession sqlSession = getSession()) {
            try {
                getClientMapper(sqlSession).depositUpdate(deposit);
            } catch (RuntimeException e) {
                LOGGER.info("Can't depositingMoney {}", e);
                sqlSession.rollback();
                List<ApiError> errorList = new ArrayList<>();
                ApiError error = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                errorList.add(error);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
    }

    public Client getClientById(int id) throws ServiceException {
        LOGGER.debug("DAO get client");
        Client client;
        try (SqlSession sqlSession = getSession()) {
            try {
                client = getUserMapper(sqlSession).infoClient(id);
                // REVU проверки надо делать в сервисе
                // метод DAO только передает данные. 
                // поэтому проверку if (client == null) надо перенести в сервис
                // равно как и в других местах
                if (client == null) {
                    sqlSession.rollback();
                    List<ApiError> errorList = new ArrayList<>();
                    ApiError error = new ApiError(ErrorCode.USER_IS_A_NOT_CLIENT.name(), "id", ErrorCode.USER_IS_A_NOT_CLIENT.getErrorString());
                    errorList.add(error);
                    throw new ServiceException(errorList);
                }
            } catch (RuntimeException e) {
                LOGGER.info("Can't get client {}", e);
                sqlSession.rollback();
                List<ApiError> errorList = new ArrayList<>();
                ApiError error = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                errorList.add(error);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return client;
    }

    public Deposit getDeposit(int clientId) throws ServiceException {
        Deposit deposit;
        try (SqlSession sqlSession = getSession()) {
            try {
                deposit = getUserMapper(sqlSession).getDeposit(clientId);
            } catch (RuntimeException e) {
                LOGGER.info("Can't get deposit {}", e);
                sqlSession.rollback();
                List<ApiError> errorList = new ArrayList<>();
                ApiError error = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), "id", e.getMessage());
                errorList.add(error);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return deposit;
    }

    public void purchase(Product purchase, Deposit deposit) throws ServiceException {
        try (SqlSession sqlSession = getSession()) {
        	// REVU избавьтесь от вложенных if-else
        	// сейчас просто трудно понять
        	// действуйте последовательно
        	// if первое действие не прошло - rollback, throw
        	// if второе действие не прошло - rollback, throw
        	// и т.д.
            try {
                if (getClientMapper(sqlSession).depositUpdate(deposit) == 1) {
                    if(getProductMapper(sqlSession).purchase(purchase) != 1){
                        sqlSession.rollback();
                        List<ApiError> errorList = new ArrayList<>();
                        // REVU если Вы передаете в new ApiError куски ErrorCode - передайте туда ErrorCode целиком, и пусть конструктор внутри разбирается с частями 
                        // кроме того, не используйте явно описанные текстовые строки вроде "deposit" - добавьте их в ErrorCode 
                        ApiError error = new ApiError(ErrorCode.INVALID_PURCHASE.name(), "deposit", ErrorCode.INVALID_PURCHASE.getErrorString());
                        errorList.add(error);
                        throw new ServiceException(errorList);
                    }
                    getProductMapper(sqlSession).addPurchases(purchase, deposit.getClientId());
                } else {
                    sqlSession.rollback();
                    List<ApiError> errorList = new ArrayList<>();
                    ApiError error = new ApiError(ErrorCode.INVALID_DEPOSIT.name(), "deposit", ErrorCode.INVALID_DEPOSIT.getErrorString());
                    errorList.add(error);
                    throw new ServiceException(errorList);
                }
            } catch (RuntimeException e) {
                LOGGER.info("Can't get deposit {}", e);
                sqlSession.rollback();
                List<ApiError> errorList = new ArrayList<>();
                ApiError error = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                errorList.add(error);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
    }
}
