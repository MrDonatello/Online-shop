package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.ProductDto;
import net.thumbtack.onlineshop.dto.response.ProductDtoDeleteResponse;
import net.thumbtack.onlineshop.dto.response.ProductDtoResponse;
import net.thumbtack.onlineshop.dto.response.ProductDtoWithCategoryName;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.service.ProductService;
import net.thumbtack.onlineshop.service.SessionService;
import net.thumbtack.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Profile({"prod", "debug"})
@RequestMapping("/api/products/")
public class ProductController {

    private final UserService userService;
    private final SessionService sessionService;
    private final ProductService productService;

    @Autowired
    public ProductController(UserService userService, SessionService sessionService, ProductService productService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.productService = productService;
    }

    @PutMapping("/{id}")
    public ProductDtoResponse editProduct(@RequestBody ProductDto productDto, @PathVariable int id, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        userService.checkAdmin(sessionService.checkSessionId(sessionId));
        productDto.setId(id);
        return productService.editProduct(productDto);
    }

    @DeleteMapping("/{id}")
    public ProductDtoDeleteResponse deleteProduct(@PathVariable int id, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        userService.checkAdmin(sessionService.checkSessionId(sessionId));
        return productService.deleteProduct(id);
    }

    @GetMapping("/{id}")
    public ProductDtoWithCategoryName getProduct(@PathVariable String id, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        sessionService.checkSessionId(sessionId);
        return productService.getProduct(id);
    }
}
