package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.CategoryDto;
import net.thumbtack.onlineshop.dto.response.CategoryDtoResponse;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.service.CategoryService;
import net.thumbtack.onlineshop.service.SessionService;
import net.thumbtack.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@Profile({"prod", "debug"})
@RequestMapping("/api/categories/")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public CategoryController(CategoryService categoryService, UserService userService, SessionService sessionService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/{id}")
    public CategoryDtoResponse getCategory(@PathVariable String id, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        userService.checkAdmin(sessionService.checkSessionId(sessionId));
        return categoryService.getCategory(id);
    }

    @PutMapping("/{id}")
    public CategoryDtoResponse editCategory(@RequestBody CategoryDto categoryDto, @PathVariable String id, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        userService.checkAdmin(sessionService.checkSessionId(sessionId));
        categoryDto.setId(id);
        return categoryService.editCategory(categoryDto);
    }

    @DeleteMapping("/{id}")
    public CategoryDtoResponse deleteCategory(@PathVariable String id, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        userService.checkAdmin(sessionService.checkSessionId(sessionId));
        return categoryService.deleteCategory(id);
    }
}
