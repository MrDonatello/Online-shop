package net.thumbtack.onlineshop.database;

import net.thumbtack.onlineshop.daoImpl.ClearDatabaseDao;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;

public class MyBatisTest {
    private static boolean setUpIsDone = false;

    private ClearDatabaseDao clearDatabaseDao = new ClearDatabaseDao();

    @BeforeClass()
    public static void setUp() {
        if (!setUpIsDone) {
            Assume.assumeTrue(MyBatisUtils.initSqlSessionFactory());
            setUpIsDone = true;
        }
    }

    @Before()
    public void clearDatabase() throws ServiceException {
        clearDatabaseDao.ClearDatabase();
    }
}