package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.model.Session;
import org.springframework.stereotype.Service;

@Service
public interface SessionDao {

    Session addSession(Session session) throws ServiceException;

    Session updateSession(Session session) throws ServiceException;

    void deleteSession(String sessionId) throws ServiceException;

    Integer checkSessionId(String sessionId) throws ServiceException;
}
