package net.thumbtack.onlineshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.daoImpl.CategoryDaoImpl;
import net.thumbtack.onlineshop.daoImpl.ClientDaoImpl;
import net.thumbtack.onlineshop.daoImpl.ProductDaoImpl;
import net.thumbtack.onlineshop.dto.request.ProductDto;
import net.thumbtack.onlineshop.dto.request.PurchaseDto;
import net.thumbtack.onlineshop.dto.response.ProductDtoDeleteResponse;
import net.thumbtack.onlineshop.dto.response.ProductDtoResponse;
import net.thumbtack.onlineshop.dto.response.ProductDtoWithCategoryName;
import net.thumbtack.onlineshop.dto.response.ProductDtoWithoutCategory;
import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.exceptions.ApiError;
import net.thumbtack.onlineshop.model.Deposit;
import net.thumbtack.onlineshop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private ProductDaoImpl productDao;
    private ObjectMapper objectMapper;
    private CategoryDaoImpl categoryDao;
    private final ClientDaoImpl clientDao;
    private LockRegistry lockProduct = new DefaultLockRegistry();
    private LockRegistry lockDeposit = new DefaultLockRegistry();


    @Autowired
    public ProductService(ProductDaoImpl productDao, ObjectMapper objectMapper, CategoryDaoImpl categoryDao, ClientDaoImpl clientDao) {
        this.productDao = productDao;
        this.objectMapper = objectMapper;
        this.categoryDao = categoryDao;
        this.clientDao = clientDao;
    }

    public ProductDtoResponse addProduct(ProductDto productDto) throws ServiceException {
        ProductDtoWithoutCategory productDtoWithoutCategory = objectMapper.convertValue(productDto, ProductDtoWithoutCategory.class);
        Product product = productDao.addProduct(objectMapper.convertValue(productDtoWithoutCategory, Product.class));
        if (productDto.getCategories() != null && productDto.getCategories().size() != 0) {
            List<Integer> categories = productDto.getCategories();
            productDao.addProductCategory(categories, product.getId());
        } else {
            productDto.setCategories(new ArrayList<>());
        }
        ProductDtoResponse productDtoResponse = objectMapper.convertValue(product, ProductDtoResponse.class);
        productDtoResponse.setCategories(productDto.getCategories());
        return productDtoResponse;
    }

    public ProductDtoResponse editProduct(ProductDto productDto) throws ServiceException {
        ProductDtoWithoutCategory productDtoWithoutCategory = objectMapper.convertValue(productDto, ProductDtoWithoutCategory.class);
        Product product = objectMapper.convertValue(productDtoWithoutCategory, Product.class);

        Product checkProductId = productDao.checkProductById(String.valueOf(product.getId()));
        if (productDto.getName() != null) {
            productDao.editProductName(product);
        } else {
            product.setName(checkProductId.getName());
        }
        if (product.getPrice() != 0) {
            productDao.editProductPrice(product);
        } else {
            product.setPrice(checkProductId.getPrice());
        }
        if (product.getCount() != 0) {
            productDao.editProductCount(product);
        } else {
            product.setCount(checkProductId.getCount());
        }
        ProductDtoResponse productDtoResponse = objectMapper.convertValue(product, ProductDtoResponse.class);
        if (productDto.getCategories() != null) {
            if (productDto.getCategories().size() == 0) {
                productDao.removeProductCategory(product.getId());
                productDtoResponse.setCategories(new ArrayList<>());
            } else {
                List<Integer> categories = productDto.getCategories();
                productDao.removeProductCategory(product.getId());
                productDao.addProductCategory(categories, product.getId());
                productDtoResponse.setCategories(productDao.getCategoryById(product.getId()));
            }
        } else {
            productDtoResponse.setCategories(productDao.getCategoryById(product.getId()));
        }
        return productDtoResponse;
    }

    public ProductDtoDeleteResponse deleteProduct(int id) throws ServiceException {
        productDao.deleteProduct(id);
        return new ProductDtoDeleteResponse();
    }

    public ProductDtoWithCategoryName getProduct(String id) throws ServiceException {
        Product product = productDao.checkProductById(id);
        ProductDtoWithCategoryName productDtoWithCategoryName;
        List<Integer> integerList = productDao.getCategoryById(Integer.parseInt(id));
        productDtoWithCategoryName = objectMapper.convertValue(product, ProductDtoWithCategoryName.class);
        productDtoWithCategoryName.setCategories(new ArrayList<>());
        for (Integer i : integerList) {
            String name = categoryDao.getCategoryName(i);
            productDtoWithCategoryName.getCategories().add(name);
        }
        return productDtoWithCategoryName;
    }

    public List<ProductDtoWithCategoryName> getListProduct(String order, List<Integer> categoryId) throws ServiceException {
        List<ProductDtoWithCategoryName> productDtoResult = new ArrayList<>();
        List<ProductDtoWithCategoryName> productWithNullCategory = new ArrayList<>();
        List<ProductDtoWithCategoryName> productWithNameCategory = new ArrayList<>();
        ProductDtoWithoutCategory productDtoWithoutCategory;
        ProductDtoWithCategoryName productDtoWithCategoryName;
        String listString;
        List<Product> products = new ArrayList<>();
        if (categoryId != null) {
            if (categoryId.size() != 0) {
                listString = categoryId.stream().map(Object::toString).collect(Collectors.joining(", "));
                List<Product> productList = productDao.getProductByCategory(listString);
                products.addAll(productList);
            } else {
                products = productDao.getProductWithOutCategory();
            }
        } else {
            products = productDao.getAllProduct();
        }
        if (order != null && order.equals("category")) {
            for (Product p : products) {
                List<Integer> categoryIdList = productDao.getCategoryById(p.getId());
                if (categoryIdList.size() != 0) {
                    productDtoWithoutCategory = objectMapper.convertValue(p, ProductDtoWithoutCategory.class);
                    for (int i : categoryIdList) {
                        List<String> category = new ArrayList<>();
                        category.add(categoryDao.getCategoryName(i));
                        ProductDtoWithCategoryName productWithName = objectMapper.convertValue(productDtoWithoutCategory, ProductDtoWithCategoryName.class);
                        productWithName.setCategories(category);
                        productWithNameCategory.add(productWithName);
                    }
                } else {
                    productDtoWithCategoryName = objectMapper.convertValue(p, ProductDtoWithCategoryName.class);
                    productDtoWithCategoryName.setCategories(new ArrayList<>());
                    productWithNullCategory.add(productDtoWithCategoryName);
                }
            }
            productDtoResult.addAll(productWithNullCategory);
            productWithNameCategory.sort(Comparator.comparing(o -> o.getCategories().get(0)));
            productDtoResult.addAll(productWithNameCategory);
        } else {
            Set<Product> setProducts = new HashSet<>(products);
            products.clear();
            products.addAll(setProducts);
            products.sort(Comparator.comparing(Product::getName));
            for (Product p : products) {
                List<Integer> categoryIdList = productDao.getCategoryById(p.getId());
                productDtoWithoutCategory = objectMapper.convertValue(p, ProductDtoWithoutCategory.class);
                List<String> category = new ArrayList<>();
                for (int i : categoryIdList) {

                    category.add(categoryDao.getCategoryName(i));
                }
                ProductDtoWithCategoryName productWithName = objectMapper.convertValue(productDtoWithoutCategory, ProductDtoWithCategoryName.class);
                productWithName.setCategories(category);
                productWithNameCategory.add(productWithName);
                productDtoResult.addAll(productWithNameCategory);
                productWithNameCategory.clear();
            }
        }
        return productDtoResult;
    }

    public PurchaseDto purchase(PurchaseDto purchaseDto) throws ServiceException {
        Lock lockP = lockProduct.obtain(purchaseDto.getId());
        Lock lockD = lockDeposit.obtain(purchaseDto.getIdClient());
        if (purchaseDto.getCount() == 0) {
            purchaseDto.setCount(1);
        }
        lockD.lock();
        lockP.lock();
        try {
            Product product = productDao.checkProductById(String.valueOf(purchaseDto.getId()));
            if (purchaseDto.getCount() <= product.getCount()) {
                if (product.getPrice() == purchaseDto.getPrice() && product.getName().equals(purchaseDto.getName())) {
                    int totalCost = product.getPrice() * purchaseDto.getCount();
                    Deposit clientDeposit = clientDao.getDeposit(purchaseDto.getIdClient());
                    if (totalCost <= clientDeposit.getDeposit()) {
                        clientDeposit.setDeposit(clientDeposit.getDeposit() - totalCost);
                        Product purchase = objectMapper.convertValue(purchaseDto, Product.class);
                        purchase.setVersion(product.getVersion());
                        clientDao.purchase(purchase, clientDeposit);
                    } else {
                        List<ApiError> apiErrorList = new ArrayList<>();
                        ApiError apiError = new ApiError(ErrorCode.INSUFFICIENT_FUNDS.name(), "price", ErrorCode.INSUFFICIENT_FUNDS.getErrorString());
                        apiErrorList.add(apiError);
                        throw new ServiceException(apiErrorList);
                    }
                } else {
                    List<ApiError> apiErrorList = new ArrayList<>();
                    ApiError apiError = new ApiError(ErrorCode.INVALID_REQUEST_PRODUCT.name(), "name,price", ErrorCode.INVALID_REQUEST_PRODUCT.getErrorString());
                    apiErrorList.add(apiError);
                    throw new ServiceException(apiErrorList);
                }
            } else {
                List<ApiError> apiErrorList = new ArrayList<>();
                ApiError apiError = new ApiError(ErrorCode.INVALID_PRODUCT_COUNT.name(), "id", ErrorCode.INVALID_PRODUCT_COUNT.getErrorString());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
        } finally {
            lockP.unlock();
            lockD.unlock();
        }
        return purchaseDto;
    }
}

