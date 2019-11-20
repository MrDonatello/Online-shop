
package net.thumbtack.onlineshop.exceptions;


public enum ErrorCode  {

    INVALID_FIRST_NAME("first name field invalid"),
    INVALID_PATRONYMIC("patronymic field invalid"),
    INVALID_ADDRESS("address field is invalid"),
    INVALID_LAST_NAME("last name field is invalid"),
    INVALID_POSITION(""),
    INVALID_PURCHASE("purchased product has been changed"),
    INVALID_EMAIL(""),
    INVALID_PRODUCT_COUNT("the required number of units of goods is not available"),
    INSUFFICIENT_FUNDS("insufficient funds in the account"),
    INVALID_REQUEST_PRODUCT("the product name or unit price indicated in the request differs from the current values for this product"),
    INVALID_LOGIN("invalid login "),
    INVALID_PHONE(""),
    INVALID_MAX_LENGTH(""),
    INVALID_MIN_LENGTH(""),
    ERROR_ADD_TO_DATABASE(""),
    DATABASE_ACCESS_ERROR("database access error"),
    PRICE_VALUE_ERROR(""),
    SESSION_ERROR(""),
    USER_IS_A_NOT_CLIENT("user is not a client"),
    NO_ACCESS_PERMISSIONS("no access permissions"),
    CATEGORY_DOES_NOT_EXIST("selected a non-existent category"),
    CLIENT_DOES_NOT_EXIST("client does not exist"),
    USER_DOES_NOT_EXIST("user does not exist"),
    PRODUCT_DOES_NOT_EXIST("selected a non-existent product"),
    CATEGORY_NOT_PARENT("category is not parent"),
    INVALID_PARENT_ID("non-existent parentId"),
    INVALID_PASSWORD("invalid password"),
    INVALID_DEPOSIT("invalid value deposit");

    private String error;

    ErrorCode(String errorString) {

        this.error = errorString;
    }

    public String getErrorString() {

        return error;
    }
}
