package net.thumbtack.onlineshop.controller;


import net.thumbtack.onlineshop.daoImpl.ClearDatabaseDao;
import net.thumbtack.onlineshop.dto.response.ClearDatabase;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Profile("debug")
public class ClearDatabaseController {

    @PostMapping("api/debug/clear")
    public ClearDatabase clearDatabase() throws ServiceException {
        ClearDatabaseDao clearDatabaseDao = new ClearDatabaseDao();
        return clearDatabaseDao.ClearDatabase();
    }
}
