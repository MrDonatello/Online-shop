package net.thumbtack.onlineshop.dto.response;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDtoWithCategoryName {

    private int id;
    private String name;
    private int price;
    private int count;
    private List<String> categories;

    public ProductDtoWithCategoryName() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
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
