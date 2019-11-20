package net.thumbtack.onlineshop.database.mappers;

import net.thumbtack.onlineshop.model.Admin;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Purchase;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AdminMapper {

    @Insert("INSERT INTO user (firstName, lastName, patronymic, login, password, role) VALUES " +
            "( #{firstName}, #{lastName}, #{patronymic}, #{login}, #{password}, #{role.name} )")
    @Options(useGeneratedKeys = true)
    void insertUser(Admin admin);

    @Insert("INSERT INTO admin (userid, position) VALUES " +
            "( #{id}, #{position} )")
    void insertAdministrator(Admin admin);

    @Select({"SELECT user.firstName, user.lastName, user.role, user.patronymic, user.id, client.userid, client.email, client.address, client.phone" +
            " FROM user, client WHERE user.id = client.userid and user.role = 'CLIENT'"})
    List<Client> getClientInfo();

    @Update("UPDATE user, admin SET user.firstName = #{firstName}, user.lastName = #{lastName}, user.patronymic = #{patronymic}, user.password = #{password}, admin.position = #{position}  WHERE user.id = #{id} and user.id = admin.userid")
    void editProfileAdmin(Admin admin);

    @SelectProvider(method = "getReport", type = net.thumbtack.onlineshop.daoImpl.PurchaseDaoProvider.class)
    List<Purchase> getReport(@Param("offsetCondition") String offsetCondition, @Param("limitCondition") String limitCondition,
                             @Param("regularizeCondition") String regularizeCondition, @Param("criterionCondition") String criterionCondition,
                             @Param("additionallyCondition") String additionallyCondition, @Param("beginDateCondition") String beginDateCondition,
                             @Param("endDateCondition") String endDateCondition);

}
