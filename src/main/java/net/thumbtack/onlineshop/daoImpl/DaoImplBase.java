package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.database.MyBatisUtils;
import net.thumbtack.onlineshop.database.mappers.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

@Service
public class DaoImplBase {

    protected SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    protected UserMapper getUserMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(UserMapper.class);
    }

    protected AdminMapper getAdministratorMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AdminMapper.class);
    }

    protected ClientMapper getClientMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(ClientMapper.class);
    }

    protected CategoryMapper getCategoryMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(CategoryMapper.class);
    }

    protected SessionMapper getSessionMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(SessionMapper.class);
    }

    protected ProductMapper getProductMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(ProductMapper.class);
    }

    protected ClearDatabaseMapper getClearDataBaseMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(ClearDatabaseMapper.class);
    }

    protected BasketMapper getBasketMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(BasketMapper.class);
    }

}
