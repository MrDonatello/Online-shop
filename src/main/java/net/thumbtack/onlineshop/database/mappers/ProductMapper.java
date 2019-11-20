package net.thumbtack.onlineshop.database.mappers;

import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ProductMapper {

    @Insert("INSERT INTO product (name, price, count) VALUES " +
            "( #{product.name}, #{product.price}, #{product.count})")
    @Options(useGeneratedKeys = true, keyProperty = "product.id")
    void addProduct(@Param("product") Product product);

    @Insert({"<script>",
            "INSERT INTO product_category (categoryid, productid) VALUES "+
            "<foreach item='item' collection = 'list' separator=','>" +
                    " ( #{item.value}, #{productId})" +
                    "</foreach>" +
                    "</script>"})
    void addProductCategory(@Param("list") List<Integer> categoryId, @Param("productId") int productId);

    @Update("UPDATE product SET product.name = #{name}, product.version = product.version + 1 WHERE product.id = #{id}")
    void editProductName(Product product);

    @Update("UPDATE product SET product.price = #{price}, product.version = product.version + 1 WHERE product.id = #{id}")
    void editProductPrice(Product product);

    @Update("UPDATE product SET product.count = #{count}, product.version = product.version + 1 WHERE product.id = #{id}")
    void editProductCount(Product product);

    @Update("UPDATE product SET product.count = product.count - #{product.count}, product.version = product.version + 1 WHERE product.id = #{product.id} and product.version = #{product.version}")
    @Options(useGeneratedKeys = true)
    int purchase(@Param("product") Product product );

    @Select("SELECT * FROM product WHERE product.id = #{id}")
    Product checkProductById(String id);

    @Select("SELECT product_category.categoryid FROM product_category WHERE productid = #{productid}")
    List<Integer> getCategoryById(int productId);

    @Delete("DELETE FROM product WHERE  product.id = #{id}")
    void deleteProduct(int id);

    @Delete("DELETE FROM product_category WHERE  productid = #{id}")
    void removeProductCategory(int id);

    @Select({"SELECT * FROM product WHERE product.id not in(SELECT product_category.productid FROM product_category)"})
    List<Product> getProductWithOutCategory();

    @Select({"SELECT * FROM product"})
    List<Product> getAllProduct();

    @Select({"SELECT * FROM product, product_category WHERE  product_category.categoryid IN (#{id}) and product.id  = product_category.productid"})
    List<Product> getProductByCategory(@Param("id")String id);

    @Insert("INSERT INTO purchases (name, productid, price, count, clientid) VALUES " +
            "( #{product.name},#{product.id}, #{product.price}, #{product.count}, #{clientId})")
    void addPurchases(@Param("product") Product product, @Param("clientId") int clientId );
}
