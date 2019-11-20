package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dto.response.ClearDatabase;
import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.exceptions.ApiError;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClearDatabaseDao extends DaoImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClearDatabase.class);

    public ClearDatabase ClearDatabase() throws ServiceException {
        LOGGER.debug("DAO Clear database");
        ClearDatabase clearDatabase = new ClearDatabase();
        List<ApiError> apiErrorList = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                getClearDataBaseMapper(sqlSession).deleteAdmin();
                getClearDataBaseMapper(sqlSession).deleteBasket();
                getClearDataBaseMapper(sqlSession).deleteCategory();
                getClearDataBaseMapper(sqlSession).deleteClien();
                getClearDataBaseMapper(sqlSession).deleteDeposit();
                getClearDataBaseMapper(sqlSession).deleteProduct();
                getClearDataBaseMapper(sqlSession).deleteProductCategory();
                getClearDataBaseMapper(sqlSession).deletePurchases();
                getClearDataBaseMapper(sqlSession).deleteSession();
                getClearDataBaseMapper(sqlSession).deleteUser();
            } catch (RuntimeException e) {
                LOGGER.info("Can't Clear database {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getMessage());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            sqlSession.commit();
        }
        return clearDatabase;
    }
}
