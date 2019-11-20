package net.thumbtack.onlineshop.database.mappers;

import net.thumbtack.onlineshop.model.Session;
import org.apache.ibatis.annotations.*;

public interface SessionMapper {

    @Insert("INSERT INTO session (javasessionid, userid) VALUES " +
            "( #{cookie.value}, #{userId} )")
    void addSession(Session session);

    @Update("UPDATE session SET javasessionid = #{cookie.value} WHERE userid = #{userId}")
    void updateSession(Session session);

    @Delete("UPDATE session SET javasessionid = NULL WHERE javasessionid = #{sessionId}")
    void delete(@Param("sessionId") String sessionId);

    @Select("SELECT userid FROM session WHERE  javasessionid = #{sessionId}")
    Integer checkSessionId(@Param("sessionId") String sessionId);
}
