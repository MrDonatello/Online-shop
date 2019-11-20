package net.thumbtack.onlineshop.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.validation.MinInterface;

public class PurchaseDto {

    @JsonIgnore
    private int idClient;
    private int id;
    private String name;
    @MinInterface(error = ErrorCode.PRICE_VALUE_ERROR, message = "the price value cannot be less than 1")
    private int price;
    private int count;

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
