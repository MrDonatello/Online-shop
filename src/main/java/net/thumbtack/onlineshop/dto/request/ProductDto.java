package net.thumbtack.onlineshop.dto.request;

import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.validation.MinInterface;

import java.util.List;

public class ProductDto {

    private int id;
    private String name;
    @MinInterface(error = ErrorCode.PRICE_VALUE_ERROR, message = "the price value cannot be less than 1")
    private int price;
    private int count;
    private List<Integer> categories;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
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
