package net.thumbtack.onlineshop.database.mappers;

import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BasketMapper {

    @Insert("INSERT INTO basket (productid, count, name, price, clientid) VALUES " +
            "( #{product.id}, #{product.count}, #{product.name}, #{product.price}, #{clientId})")
    void addProductToBasket(@Param("product") Product product, @Param("clientId") int clientId);

    @Select("SELECT basket.id FROM basket WHERE basket.productid = #{productId} and basket.clientid = #{clientId}")
    Integer isHaveProduct(@Param("productId") int productId, @Param("clientId") int clientId);

    @Update("UPDATE  basket SET basket.count = basket.count + #{count} WHERE basket.productid = #{productId} and basket.clientid = #{clientId}")
    void updateBasket(@Param("productId") int productId, @Param("count") int count, @Param("clientId") int clientId);

    @Update("UPDATE  basket SET basket.count = #{count} WHERE basket.productid = #{productId} and basket.clientid = #{clientId}")
    void updateBasketCount(@Param("productId") int productId, @Param("count") int count, @Param("clientId") int clientId);

    @Select("SELECT basket.clientid AS clientId, basket.productid, basket.productid AS id, basket.count, basket.name, basket.price FROM basket WHERE basket.clientid = #{clientId}")
    List<Product> getFullBasket(int clientId);

    @Delete("DELETE  FROM basket WHERE  basket.clientid = #{clientId} and basket.productid = #{productId}")
    void deleteItemFromBasket(@Param("clientId") int clientId, @Param("productId") int productId);

    @Delete("DELETE  FROM basket WHERE  basket.clientid = #{clientId} and basket.productid IS NULL")
    void deleteRemovedProducts(@Param("clientId") int clientId);
}
