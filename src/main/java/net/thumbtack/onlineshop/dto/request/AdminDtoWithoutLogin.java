package net.thumbtack.onlineshop.dto.request;

import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.model.Role;
import net.thumbtack.onlineshop.validation.MaxNameLength;
import net.thumbtack.onlineshop.validation.MinPasswordLength;
import net.thumbtack.onlineshop.validation.NotNullInterface;
import net.thumbtack.onlineshop.validation.PatternInterface;

public class AdminDtoWithoutLogin {

    @PatternInterface(regexp = "^[А-Яа-я- ]+$", message = "the first name can contain only Russian letters, spaces and the minus sign", error = ErrorCode.INVALID_FIRST_NAME)
    @MaxNameLength
    private String firstName;
    @PatternInterface(regexp = "^[А-Яа-я- ]+$", message = "the last name can contain only Russian letters, spaces and the minus sign", error = ErrorCode.INVALID_LAST_NAME)
    @MaxNameLength
    private String lastName;
    @PatternInterface(regexp = "(^$)|(^[А-Яа-я- ]+$)", message = "the patronymic can contain only Russian letters, spaces and the minus sign", error = ErrorCode.INVALID_PATRONYMIC)
    @MaxNameLength
    private String patronymic;
    private Role role;
    @MinPasswordLength
    @MaxNameLength
    @NotNullInterface(error = ErrorCode.INVALID_PASSWORD, message = "Password cannot be null")
    private String newPassword;
    @MaxNameLength()
    private String position;
    private String oldPassword;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public AdminDtoWithoutLogin() {
        this.role = Role.ADMIN;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
}
