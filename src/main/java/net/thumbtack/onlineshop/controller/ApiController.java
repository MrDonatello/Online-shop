package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.*;
import net.thumbtack.onlineshop.dto.response.*;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
@Profile({"prod", "debug"})
public class ApiController {

    private final AdminService adminService;
    private final UserService userService;
    private final ClientService clientService;
    private final SessionService sessionService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final BasketService basketService;
    private final SettingsService settingsService;

    @Autowired
    public ApiController(AdminService adminService, UserService userService, ClientService clientService, SessionService sessionService, CategoryService categoryService, ProductService productService, BasketService basketService, SettingsService settingsService) {
        this.userService = userService;
        this.clientService = clientService;
        this.adminService = adminService;
        this.sessionService = sessionService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.basketService = basketService;
        this.settingsService = settingsService;
    }


    @PostMapping("/api/admins")
    public UserDtoResponse addAdmin(@RequestBody @Valid AdminDto adminDto, HttpServletResponse response) throws ServiceException {
        UserDtoResponse userDtoResponse = adminService.insert(adminDto);
        response.addCookie(sessionService.createNewSession(userDtoResponse.getId()));
        return userDtoResponse;
    }

    @PostMapping("/api/clients")
    public UserDtoResponse addClient(@RequestBody @Valid ClientDto clientDto, HttpServletResponse response) throws ServiceException {
        UserDtoResponse userDtoResponse = clientService.insert(clientDto);
        response.addCookie(sessionService.createNewSession(userDtoResponse.getId()));
        return userDtoResponse;
    }

    @PostMapping("/api/sessions")
    public UserDtoResponse login(@RequestBody LoginPasswordDto loginPasswordDto, HttpServletResponse response) throws ServiceException {
        UserDtoResponse userDtoResponse = userService.login(loginPasswordDto);
        response.addCookie(sessionService.updateSession(userDtoResponse.getId()));
        return userDtoResponse;
    }

    @DeleteMapping("/api/sessions" )
    public SessionDtoResponse logout(@CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        return sessionService.deleteSession(sessionId);
    }

    @GetMapping("/api/accounts")
    public UserDtoResponse infoAccounts(@CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        return userService.infoAccounts(sessionService.checkSessionId(sessionId));
    }

    @GetMapping("/api/clients")
    public List<UserDtoResponse> infoClient(@CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        userService.checkAdmin(sessionService.checkSessionId(sessionId));
        return adminService.infoClient();
    }

    @PutMapping("/api/admins")
    public UserDtoResponse editAdminProfile(@RequestBody @Valid AdminDtoWithoutLogin adminDto, @CookieValue("JAVASESSIONID") String sessionId, HttpServletResponse response) throws ServiceException {
        int id = sessionService.checkSessionId(sessionId);
        userService.checkAdmin(id);
        adminDto.setId(id);
        UserDtoResponse userDtoResponse = adminService.editAdminProfile(adminDto);
        response.addCookie(sessionService.createNewSession(userDtoResponse.getId()));
        return userDtoResponse;
    }

    @PutMapping("/api/clients")
    public UserDtoResponse editClientProfile(@RequestBody @Valid ClientDtoWithoutLogin clientDto, @CookieValue("JAVASESSIONID") String sessionId, HttpServletResponse response) throws ServiceException {
        int id = sessionService.checkSessionId(sessionId);
        userService.checkClient(id);
        clientDto.setId(id);
        UserDtoResponse userDtoResponse = clientService.editClientProfile(clientDto);
        response.addCookie(sessionService.createNewSession(userDtoResponse.getId()));
        return userDtoResponse;
    }

    @PostMapping("/api/categories")
    public CategoryDtoResponse addCategoryOrSubcategory(@RequestBody CategoryDto categoryDto, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        userService.checkAdmin(sessionService.checkSessionId(sessionId));
        return categoryService.addCategory(categoryDto);
    }

    @GetMapping("/api/categories")
    public List<CategoryDtoResponse> getAllCategoryOrSubcategory(@CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        userService.checkAdmin(sessionService.checkSessionId(sessionId));
        return categoryService.getAllCategory();
    }

    @PostMapping("/api/products")
    public ProductDtoResponse addProduct(@RequestBody @Valid ProductDto productDto, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        userService.checkAdmin(sessionService.checkSessionId(sessionId));
        return productService.addProduct(productDto);
    }

    @GetMapping("/api/products")
    public List<ProductDtoWithCategoryName> getAllProduct(@RequestParam(required = false) List<Integer> category, String order, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        sessionService.checkSessionId(sessionId);
        return productService.getListProduct(order, category);
    }

    @PutMapping("/api/deposits")
    public UserDtoResponse depositingMoney(@RequestBody @Valid DepositDto depositDto, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        int clientId = sessionService.checkSessionId(sessionId);
        depositDto.setClientId(clientId);
        return clientService.depositingMoney(depositDto);
    }

    @GetMapping("/api/deposits")
    public UserDtoResponse getDeposit(@CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        int clientId = sessionService.checkSessionId(sessionId);
        return clientService.getDeposit(clientId);
    }

    @PostMapping("/api/purchases")
    public PurchaseDto purchase(@RequestBody @Valid PurchaseDto purchaseDto, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        int clientId = sessionService.checkSessionId(sessionId);
        userService.checkClient(clientId);
        purchaseDto.setIdClient(clientId);
        return productService.purchase(purchaseDto);
    }

    @PostMapping("/api/baskets")
    public List<ProductDtoWithoutCategory> addProductToBasket(@RequestBody @Valid ProductDto productDto, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        int clientId = sessionService.checkSessionId(sessionId);
        userService.checkClient(clientId);
        return basketService.addProductToBasket(productDto, clientId);
    }

    @DeleteMapping("api/baskets/{productId}")
    public ProductDtoDeleteResponse deleteItemFromBasket(@PathVariable int productId, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        int clientId = sessionService.checkSessionId(sessionId);
        userService.checkClient(clientId);
        return basketService.deleteItemFromBasket(productId, clientId);
    }

    @PutMapping("api/baskets")
    public List<ProductDtoWithoutCategory> editBasket(@RequestBody @Valid ProductDto productDto, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        int clientId = sessionService.checkSessionId(sessionId);
        userService.checkClient(clientId);
        return basketService.editBasket(productDto, clientId);
    }

    @GetMapping("api/baskets")
    public List<ProductDtoWithoutCategory> getAllBasket(@CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        int clientId = sessionService.checkSessionId(sessionId);
        userService.checkClient(clientId);
        return basketService.getAllBasket(clientId);
    }

    @PostMapping("/api/purchases/baskets")
    public PurchasesFromBasketDtoResponse purchasesFromBasket(@RequestBody @Valid List<ProductDtoWithoutCategory> productDtoWithoutCategory, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        int clientId = sessionService.checkSessionId(sessionId);
        userService.checkClient(clientId);
        return basketService.purchasesFromBasket(productDtoWithoutCategory, clientId);
    }

    @GetMapping("/api/purchases")
    public ReportDtoResponse getReport(@RequestParam(required = false) Integer offset, Integer limit, String results, String regularize, String criterion, @RequestParam(required = false) List<Integer> additionally, @RequestParam(required = false) List<String> date, @CookieValue("JAVASESSIONID") String sessionId) throws ServiceException {
        userService.checkAdmin(sessionService.checkSessionId(sessionId));
        return adminService.getReport(offset, limit, results, regularize, criterion, additionally, date);
    }

    @GetMapping("api/settings")
    public SettingsDtoResponse getSettings(@CookieValue(name = "JAVASESSIONID", required = false) String sessionId) {
        return settingsService.getSettings(sessionId);
    }


}