package net.thumbtack.onlineshop.database.mappers;

import org.apache.ibatis.annotations.Delete;

public interface ClearDatabaseMapper {


    @Delete("DELETE FROM admin")
    void deleteAdmin();

    @Delete("DELETE FROM basket")
    void deleteBasket();

    @Delete("DELETE FROM  category")
    void deleteCategory();

    @Delete("DELETE FROM  client")
    void deleteClien();

    @Delete("DELETE FROM  deposit")
    void deleteDeposit();

    @Delete("DELETE FROM  product")
    void deleteProduct();

    @Delete("DELETE FROM  product_category")
    void deleteProductCategory();

    @Delete("DELETE FROM  purchases")
    void deletePurchases();

    @Delete("DELETE FROM session")
    void deleteSession();

    @Delete("DELETE FROM  user")
    void deleteUser();
}
