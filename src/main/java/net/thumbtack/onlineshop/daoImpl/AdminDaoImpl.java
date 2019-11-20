package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.AdminDao;
import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.model.Admin;
import net.thumbtack.onlineshop.exceptions.ApiError;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Purchase;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public class AdminDaoImpl extends DaoImplBase implements AdminDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(Admin.class);

    public Admin insert(Admin admin) throws ServiceException {
        LOGGER.debug("DAO insert Administrator {}", admin);
        List<ApiError> errorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdministratorMapper(sqlSession).insertUser(admin);
                getAdministratorMapper(sqlSession).insertAdministrator(admin);
            } catch (RuntimeException e) {
                LOGGER.info("Can't insert Administrator {}, {}", admin, e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.ERROR_ADD_TO_DATABASE.name(), null, e.getCause().getMessage());
                errorList.add(apiError);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return admin;
    }

    public Admin editAdminProfile(Admin admin) throws ServiceException {
        LOGGER.debug("DAO Edit Administrator {}", admin);
        List<ApiError> errorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdministratorMapper(sqlSession).editProfileAdmin(admin);
            } catch (RuntimeException e) {
                LOGGER.info("Can't Edit Administrator {}, {}", admin, e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                errorList.add(apiError);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return admin;
    }

    public List<Client> getClientInfo() throws ServiceException {
        LOGGER.debug("DAO get client Info");
        List<ApiError> errorList = new ArrayList<>();
        List<Client> clientList;
        try (SqlSession sqlSession = getSession()) {
            try {
                clientList = getAdministratorMapper(sqlSession).getClientInfo();
            } catch (RuntimeException e) {
                LOGGER.info("Can't get client Info", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                errorList.add(apiError);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return clientList;
    }

    public List<Purchase> getReport(Map<String, String> map) throws ServiceException {
        LOGGER.debug("DAO get report");
        List<ApiError> errorList = new ArrayList<>();
        List<Purchase> purchaseList;
        String offsetCondition = null;
        String limitCondition = null;
        String regularizeCondition = null;
        String criterionCondition = null;
        String additionallyCondition = null;
        String beginDateCondition = null;
        String endDateCondition = null;
        if (map != null) {
            offsetCondition = map.get("offset");
            limitCondition = map.get("limit");
            regularizeCondition = map.get("regularize");
            criterionCondition = map.get("criterion");
            additionallyCondition = map.get("additionally");
            beginDateCondition = map.get("dateBeginning");
            endDateCondition = map.get("dateEnd");
        }
        try (SqlSession sqlSession = getSession()) {
            try {
                purchaseList = getAdministratorMapper(sqlSession).getReport(offsetCondition, limitCondition, regularizeCondition,
                        criterionCondition, additionallyCondition, beginDateCondition, endDateCondition);
            } catch (RuntimeException e) {
                LOGGER.info("Can't get report", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                errorList.add(apiError);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return purchaseList;
    }
}
