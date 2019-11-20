package net.thumbtack.onlineshop.dto.response;

import java.util.List;

public class PurchasesFromBasketDtoResponse {

    private List<ProductDtoWithoutCategory> bought;

    private List<ProductDtoWithoutCategory> remaining;

    public List<ProductDtoWithoutCategory> getBought() {
        return bought;
    }

    public void setBought(List<ProductDtoWithoutCategory> bought) {
        this.bought = bought;
    }

    public List<ProductDtoWithoutCategory> getRemaining() {
        return remaining;
    }

    public void setRemaining(List<ProductDtoWithoutCategory> remaining) {
        this.remaining = remaining;
    }
}
