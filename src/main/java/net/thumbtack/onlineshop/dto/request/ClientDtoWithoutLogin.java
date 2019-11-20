package net.thumbtack.onlineshop.dto.request;

import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.model.Role;
import net.thumbtack.onlineshop.validation.*;

public class ClientDtoWithoutLogin {

    @EmailInterface(message = "Email should be valid", error = ErrorCode.INVALID_EMAIL)
    private String email;

    @MaxNameLength
    @NotNullInterface(message = "Address cannot be null", error = ErrorCode.INVALID_ADDRESS)
    private String address;

    @PatternInterface(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{10,15}$", message = "invalid phone", error = ErrorCode.INVALID_PHONE)
    private String phone;


    @PatternInterface(regexp = "^[А-Яа-я- ]+$", message = "the first name can contain only Russian letters, spaces and the minus sign", error = ErrorCode.INVALID_FIRST_NAME)
    @MaxNameLength
    private String firstName;

    @PatternInterface(regexp = "^[А-Яа-я- ]+$", message = "the last name can contain only Russian letters, spaces and the minus sign", error = ErrorCode.INVALID_LAST_NAME)
    @MaxNameLength
    private String lastName;

    @PatternInterface(regexp = "(^$)|(^[А-Яа-я- ]+$)", message = "the patronymic can contain only Russian letters, spaces and the minus sign", error = ErrorCode.INVALID_PATRONYMIC)
    @MaxNameLength
    private String patronymic;

    private String oldPassword;

    @MinPasswordLength
    @MaxNameLength
    @NotNullInterface(message = "Password cannot be null", error = ErrorCode.INVALID_PASSWORD)
    private String newPassword;

    private Role role;

    private int id;

    public ClientDtoWithoutLogin() {
        this.role = Role.CLIENT;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
