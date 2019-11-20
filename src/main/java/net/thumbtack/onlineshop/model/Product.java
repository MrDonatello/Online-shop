package net.thumbtack.onlineshop.model;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class Product {

    private int id;
    private String name;
    private int price;
    private int count;
    private List<Category> categories;
    private int version;

    public Product() {
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getId() == product.getId() &&
                Double.compare(product.getPrice(), getPrice()) == 0 &&
                getCount() == product.getCount() &&
                Objects.equals(getName(), product.getName()) &&
                Objects.equals(getCategories(), product.getCategories());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getPrice(), getCount(), getCategories());
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
