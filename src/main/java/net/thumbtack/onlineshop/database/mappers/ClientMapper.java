package net.thumbtack.onlineshop.database.mappers;

import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Deposit;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

public interface ClientMapper {

    @Insert("INSERT INTO user (firstName, lastName, patronymic, login, password, role) VALUES " +
            "( #{firstName}, #{lastName}, #{patronymic}, #{login}, #{password}, #{role.name} )")
    @Options(useGeneratedKeys = true)
    void insertUser(Client client);

    @Insert("INSERT INTO client ( userid, email, address, phone) VALUES " +
            "(#{id}, #{email}, #{address}, #{phone})")
    void insertClient(Client client);

    @Insert("INSERT INTO deposit (clientid) VALUES " +
            "(#{id})")
    void addDeposit(Client client);

    @Update("UPDATE deposit SET deposit.deposit = #{deposit}, deposit.version = deposit.version + 1 WHERE deposit.clientid = #{clientId} and deposit.version = #{version}")
    @Options(useGeneratedKeys = true)
    int depositUpdate(Deposit deposit);

    @Update("UPDATE user, client SET user.firstName = #{firstName}, user.lastName = #{lastName}, " +
            "user.patronymic = #{patronymic}, user.password = #{password}, client.email = #{email}, " +
            "client.address = #{address}, client.phone = #{phone}  WHERE user.id = #{id} and user.id = client.userid")
    void EditProfileClient(Client client);
}
