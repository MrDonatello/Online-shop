package net.thumbtack.onlineshop.database.mappers;

import net.thumbtack.onlineshop.model.Admin;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Deposit;
import net.thumbtack.onlineshop.model.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

public interface UserMapper {

    @Select({"SELECT user.firstName, user.lastName, user.role, user.patronymic, user.id, admin.position, admin.userid" +
            " FROM user, admin WHERE user.login = #{login} and user.password = #{password} and user.id = admin.userid"})
    Admin loginAdmin(@Param("login") String login, @Param("password") String password);

    @Select({"SELECT user.firstName, user.lastName, user.role, user.patronymic, user.id, admin.position, admin.userid" +
            " FROM user, admin WHERE user.id = #{id} and user.id = admin.userid"})
    Admin infoAdmin(int id);

    @Select({"SELECT user.role FROM user WHERE user.login = #{login} and user.password = #{password}"})
    Role getRole(@Param("login") String login, @Param("password") String password);

    @Select({"SELECT user.role FROM user WHERE user.id = #{id}"})
    Role getRoleById(int id);

    @Select("SELECT user.firstName, user.lastName, user.patronymic, user.role, user.id AS idd, user.id, client.email, client.address, client.phone,  client.userid " +
            "FROM user, client WHERE user.login = #{login} and user.password = #{password} and user.id = client.userid ")
    @Results({
            @Result(property = "idd", column = "user.id"),
            @Result(property = "deposit", column = "idd", javaType = Deposit.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mappers.UserMapper.getDeposit", fetchType = FetchType.DEFAULT))
    })
    Client loginClient(@Param("login") String login, @Param("password") String password);

    @Select("SELECT user.firstName, user.lastName, user.patronymic, user.role, user.id AS idd, user.id, client.email, client.address, client.phone,  client.userid " +
            "FROM user, client WHERE user.id = #{id} and user.id = client.userid ")
    @Results({
            @Result(property = "idd", column = "user.id"),
            @Result(property = "deposit", column = "idd", javaType = Deposit.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mappers.UserMapper.getDeposit", fetchType = FetchType.DEFAULT))
    })
    Client infoClient(int id);

    @Select("SELECT deposit.deposit, deposit.clientid, deposit.version " +
            "FROM  deposit WHERE deposit.clientid = #{id}")
    Deposit getDeposit(int id);
}
