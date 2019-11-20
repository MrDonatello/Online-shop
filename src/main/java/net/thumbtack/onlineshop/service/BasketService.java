package net.thumbtack.onlineshop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.daoImpl.BasketDaoImpl;
import net.thumbtack.onlineshop.daoImpl.ClientDaoImpl;
import net.thumbtack.onlineshop.daoImpl.ProductDaoImpl;
import net.thumbtack.onlineshop.dto.request.ProductDto;
import net.thumbtack.onlineshop.dto.response.ProductDtoDeleteResponse;
import net.thumbtack.onlineshop.dto.response.ProductDtoWithVersion;
import net.thumbtack.onlineshop.dto.response.ProductDtoWithoutCategory;
import net.thumbtack.onlineshop.dto.response.PurchasesFromBasketDtoResponse;
import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.exceptions.ApiError;
import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.Deposit;
import net.thumbtack.onlineshop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

@Service
public class BasketService {

    private final BasketDaoImpl basketDao;
    private ProductDaoImpl productDao;
    private ObjectMapper objectMapper;
    private final ClientDaoImpl clientDao;
    private LockRegistry lockDeposit = new DefaultLockRegistry();

    @Autowired
    public BasketService(BasketDaoImpl basketDao, ProductDaoImpl productDao, ObjectMapper objectMapper, ClientDaoImpl clientDao) {
        this.basketDao = basketDao;
        this.productDao = productDao;
        this.objectMapper = objectMapper;
        this.clientDao = clientDao;
    }

    public List<ProductDtoWithoutCategory> addProductToBasket(ProductDto productDto, int id) throws ServiceException {
        List<ProductDtoWithoutCategory> productDtoWithoutCategory = new ArrayList<>();
        Product product = productDao.checkProductById(String.valueOf(productDto.getId()));
        if (productDto.getCount() == 0) {
            productDto.setCount(1);
        }
        if (product.getPrice() == productDto.getPrice() && product.getName().equals(productDto.getName())) {
            Basket basket = new Basket();
            basket.setClientId(id);
            Product productAdd = objectMapper.convertValue(productDto, Product.class);
            List<Product> productList = new ArrayList<>();
            productList.add(productAdd);
            basket.setProducts(productList);
            if (basketDao.isPresentProduct(basket)) {
                basketDao.updateBasket(basket);
            } else {
                basketDao.addProductToBasket(basket);
            }
            for (Product p : basketDao.getFullBasket(basket.getClientId())) {
                productDtoWithoutCategory.add(objectMapper.convertValue(p, ProductDtoWithoutCategory.class));
            }
        } else {
            List<ApiError> apiErrorList = new ArrayList<>();
            ApiError apiError = new ApiError(ErrorCode.INVALID_REQUEST_PRODUCT.name(), "name,price", ErrorCode.INVALID_REQUEST_PRODUCT.getErrorString());
            apiErrorList.add(apiError);
            throw new ServiceException(apiErrorList);
        }
        return productDtoWithoutCategory;
    }

    public List<ProductDtoWithoutCategory> editBasket(ProductDto productDto, int id) throws ServiceException {
        List<ProductDtoWithoutCategory> productDtoWithoutCategory = new ArrayList<>();
        Product product = productDao.checkProductById(String.valueOf(productDto.getId()));
        if (product.getPrice() == productDto.getPrice() && product.getName().equals(productDto.getName())) {
            Basket basket = new Basket();
            basket.setClientId(id);
            Product productAdd = objectMapper.convertValue(productDto, Product.class);
            List<Product> productList = new ArrayList<>();
            productList.add(productAdd);
            basket.setProducts(productList);
            basketDao.updateBasketCount(basket);
            for (Product p : basketDao.getFullBasket(basket.getClientId())) {
                productDtoWithoutCategory.add(objectMapper.convertValue(p, ProductDtoWithoutCategory.class));
            }
        } else {
            List<ApiError> apiErrorList = new ArrayList<>();
            ApiError apiError = new ApiError(ErrorCode.INVALID_REQUEST_PRODUCT.name(), "name,price", ErrorCode.INVALID_REQUEST_PRODUCT.getErrorString());
            apiErrorList.add(apiError);
            throw new ServiceException(apiErrorList);
        }
        return productDtoWithoutCategory;
    }

    public ProductDtoDeleteResponse deleteItemFromBasket(int productId, int clientId) throws ServiceException {
        basketDao.deleteItemFromBasket(clientId, productId);
        return new ProductDtoDeleteResponse();
    }

    public List<ProductDtoWithoutCategory> getAllBasket(int clientId) throws ServiceException {
        List<ProductDtoWithoutCategory> productDtoWithoutCategory = new ArrayList<>();
        for (Product p : basketDao.getFullBasket(clientId)) {
            productDtoWithoutCategory.add(objectMapper.convertValue(p, ProductDtoWithoutCategory.class));
        }
        return productDtoWithoutCategory;
    }

    public PurchasesFromBasketDtoResponse purchasesFromBasket(List<ProductDtoWithoutCategory> productDtoWithoutCategory, int clientId) throws ServiceException {
        PurchasesFromBasketDtoResponse purchasesFromBasketDtoResponse = new PurchasesFromBasketDtoResponse();
        List<ProductDtoWithVersion> provenProduct = new ArrayList<>();
        List<ProductDtoWithoutCategory> rejectedProduct = new ArrayList<>();
        Lock lockD = lockDeposit.obtain(clientId);
        int totalCost = 0;
        Product product;
        Product productInBasket;
        List<Product> fullBasket = basketDao.getFullBasket(clientId);
        for (ProductDtoWithoutCategory productDto : productDtoWithoutCategory) {
            try {
                product = productDao.checkProductById(String.valueOf(productDto.getId()));
                productInBasket = fullBasket.stream().filter(item -> item.getId() == productDto.getId()).findFirst().get();
            } catch (ServiceException | RuntimeException e) {
                continue;
            }
            if (productDto.getCount() == 0 || productDto.getCount() > productInBasket.getCount()) {
                productDto.setCount(productInBasket.getCount());
            }
            if (product.getPrice() == productDto.getPrice() && product.getName().equals(productDto.getName()) && productDto.getCount() <= product.getCount()) {
                totalCost += product.getPrice() * productDto.getCount();
                ProductDtoWithVersion dtoWithVersion = objectMapper.convertValue(productDto, ProductDtoWithVersion.class);
                dtoWithVersion.setVersion(product.getVersion());
                provenProduct.add(dtoWithVersion);
            }
        }
        lockD.lock();
        try {
            Deposit clientDeposit = clientDao.getDeposit(clientId);
            Basket basket = new Basket();
            List<Product> productList = new ArrayList<>();
            if (totalCost <= clientDeposit.getDeposit()) {
                clientDeposit.setDeposit(clientDeposit.getDeposit() - totalCost);
                for (ProductDtoWithVersion p : provenProduct) {
                    productList.add(objectMapper.convertValue(p, Product.class));
                }
                basket.setClientId(clientId);
                basket.setProducts(productList);
                basketDao.purchasesFromBasket(basket, clientDeposit);
            } else {
                provenProduct.clear();
            }
        } finally {
            lockD.unlock();
        }
        for (Product p : basketDao.getFullBasket(clientId)) {
            rejectedProduct.add(objectMapper.convertValue(p, ProductDtoWithoutCategory.class));
        }
        purchasesFromBasketDtoResponse.setBought(objectMapper.convertValue(provenProduct, new TypeReference<List<ProductDtoWithoutCategory>>() {
        }));
        purchasesFromBasketDtoResponse.setRemaining(rejectedProduct);

        return purchasesFromBasketDtoResponse;
    }
}
