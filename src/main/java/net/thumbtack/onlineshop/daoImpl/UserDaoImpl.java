package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.UserDao;
import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.exceptions.ApiError;
import net.thumbtack.onlineshop.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl extends DaoImplBase implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    public User login(String login, String password) throws ServiceException {
        List<ApiError> errorList = new ArrayList<>();
        User user;
        LOGGER.debug("DAO login start");
        try (SqlSession sqlSession = getSession()) {
            try {
                user = getUserMapper(sqlSession).getRole(login, password).name().equals("ADMIN")
                        ? getUserMapper(sqlSession).loginAdmin(login, password)
                        : getUserMapper(sqlSession).loginClient(login, password);
            } catch (RuntimeException e) {
                LOGGER.info("Can't login {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                errorList.add(apiError);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return user;
    }

    public User infoAccounts(int userId) throws ServiceException {
        List<ApiError> errorList = new ArrayList<>();
        User user;
        LOGGER.debug("DAO infoAccounts start");
        try (SqlSession sqlSession = getSession()) {
            try {
                user = getUserMapper(sqlSession).getRoleById(userId).name().equals("ADMIN")
                        ? getUserMapper(sqlSession).infoAdmin(userId)
                        : getUserMapper(sqlSession).infoClient(userId);
            } catch (RuntimeException e) {
                LOGGER.info("Can't infoAccounts {}", e);
                sqlSession.rollback();
                ApiError apiError = new ApiError(ErrorCode.DATABASE_ACCESS_ERROR.name(), null, e.getCause().getMessage());
                errorList.add(apiError);
                throw new ServiceException(errorList);
            }
            sqlSession.commit();
        }
        return user;
    }
}
