package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.daoImpl.AdminDaoImpl;
import net.thumbtack.onlineshop.daoImpl.SessionDaoImpl;
import net.thumbtack.onlineshop.daoImpl.UserDaoImpl;
import net.thumbtack.onlineshop.database.MyBatisTest;
import net.thumbtack.onlineshop.dto.request.*;
import net.thumbtack.onlineshop.dto.response.CategoryDtoResponse;
import net.thumbtack.onlineshop.dto.response.UserDtoResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApiControllerTest extends MyBatisTest {

    private RestTemplate template = new RestTemplate();

    private AdminDto adminDto = new AdminDto("Иван", "Иванов", "", "Логин", "Пароль", "админ");
    private ClientDto clientDto = new ClientDto("Петр", "Петров", "Петрович", "123@.com", "адрес", "89123456789", "ЛогинКлиент", "ПарольКлиент");

    @Autowired
    AdminDaoImpl adminDao;

    @Autowired
    UserDaoImpl userDao;

    @Autowired
    SessionDaoImpl sessionDao;

    @Test
    public void addAdmin() {
        ResponseEntity<UserDtoResponse> result = template.postForEntity("http://localhost:8080/api/admins", adminDto, UserDtoResponse.class);
        String cookie = result.getHeaders().getFirst("Set-Cookie");
        assertEquals(adminDto.getFirstName(), result.getBody().getFirstName());
        assertEquals(adminDto.getLastName(), result.getBody().getLastName());
        assertEquals(adminDto.getPatronymic(), result.getBody().getPatronymic());
        assertEquals(adminDto.getPosition(), result.getBody().getPosition());
        assertNotEquals(0, result.getBody().getId());
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(cookie);
    }

    @Test
    public void addClient() {
        ResponseEntity<UserDtoResponse> result = template.postForEntity("http://localhost:8080/api/clients", clientDto, UserDtoResponse.class);
        String cookie = result.getHeaders().getFirst("Set-Cookie");
        assertEquals(clientDto.getFirstName(), result.getBody().getFirstName());
        assertEquals(clientDto.getLastName(), result.getBody().getLastName());
        assertEquals(clientDto.getPatronymic(), result.getBody().getPatronymic());
        assertEquals(clientDto.getEmail(), result.getBody().getEmail());
        assertEquals(clientDto.getAddress(), result.getBody().getAddress());
        assertEquals(clientDto.getPhone(), result.getBody().getPhone());
        assertNotEquals(0, result.getBody().getId());
        assertEquals("0", result.getBody().getDeposit());
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(cookie);
    }

    @Test
    public void loginLogoutAdmin() {
        ResponseEntity<UserDtoResponse> resultInsert = template.postForEntity("http://localhost:8080/api/admins", adminDto, UserDtoResponse.class);
        String cookieInsert = resultInsert.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookieInsert);

        ResponseEntity<String> resultDelete = template.exchange("http://localhost:8080/api/sessions", HttpMethod.DELETE, new HttpEntity<String>(headers), String.class);
        assertEquals("{}", resultDelete.getBody());

        LoginPasswordDto loginPasswordDto = new LoginPasswordDto();
        loginPasswordDto.setLogin("Логин");
        loginPasswordDto.setPassword("Пароль");

        ResponseEntity<UserDtoResponse> resultLogin = template.postForEntity("http://localhost:8080/api/sessions", loginPasswordDto, UserDtoResponse.class);
        String cookieLogin = resultLogin.getHeaders().getFirst("Set-Cookie");
        assertNotNull(cookieLogin);
        assertNotEquals(cookieLogin, cookieInsert);
        assertEquals(adminDto.getFirstName(), resultLogin.getBody().getFirstName());
        assertEquals(adminDto.getLastName(), resultLogin.getBody().getLastName());
        assertEquals(adminDto.getPatronymic(), resultLogin.getBody().getPatronymic());
        assertEquals(adminDto.getPosition(), resultLogin.getBody().getPosition());
        assertNotEquals(0, resultLogin.getBody().getId());
        assertEquals(200, resultLogin.getStatusCode().value());
    }

    @Test
    public void loginLogoutClient() {
        ResponseEntity<UserDtoResponse> resultInsert = template.postForEntity("http://localhost:8080/api/clients", clientDto, UserDtoResponse.class);
        String cookieInsert = resultInsert.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookieInsert);

        ResponseEntity<String> resultDelete = template.exchange("http://localhost:8080/api/sessions", HttpMethod.DELETE, new HttpEntity<String>(headers), String.class);
        assertEquals("{}", resultDelete.getBody());

        LoginPasswordDto loginPasswordDto = new LoginPasswordDto();
        loginPasswordDto.setLogin("ЛогинКлиент");
        loginPasswordDto.setPassword("ПарольКлиент");

        ResponseEntity<UserDtoResponse> resultLogin = template.postForEntity("http://localhost:8080/api/sessions", loginPasswordDto, UserDtoResponse.class);
        String cookieLogin = resultLogin.getHeaders().getFirst("Set-Cookie");
        assertNotNull(cookieLogin);
        assertNotEquals(cookieLogin, cookieInsert);
        assertEquals(clientDto.getFirstName(), resultLogin.getBody().getFirstName());
        assertEquals(clientDto.getLastName(), resultLogin.getBody().getLastName());
        assertEquals(clientDto.getPatronymic(), resultLogin.getBody().getPatronymic());
        assertEquals(clientDto.getEmail(), resultLogin.getBody().getEmail());
        assertEquals(clientDto.getAddress(), resultLogin.getBody().getAddress());
        assertEquals(clientDto.getPhone(), resultLogin.getBody().getPhone());
        assertNotEquals(0, resultLogin.getBody().getId());
        assertEquals(200, resultLogin.getStatusCode().value());
    }

    @Test
    public void infoAccountsAdmin() {
        ResponseEntity<UserDtoResponse> resultInsert = template.postForEntity("http://localhost:8080/api/admins", adminDto, UserDtoResponse.class);
        String cookieInsert = resultInsert.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookieInsert);

        ResponseEntity<UserDtoResponse> resultInfo = template.exchange("http://localhost:8080/api/accounts", HttpMethod.GET, new HttpEntity<String>(headers), UserDtoResponse.class);
        assertEquals(adminDto.getFirstName(), resultInfo.getBody().getFirstName());
        assertEquals(adminDto.getLastName(), resultInfo.getBody().getLastName());
        assertEquals(adminDto.getPatronymic(), resultInfo.getBody().getPatronymic());
        assertEquals(adminDto.getPosition(), resultInfo.getBody().getPosition());
        assertNotEquals(0, resultInfo.getBody().getId());
        assertEquals(200, resultInfo.getStatusCode().value());
    }

    @Test
    public void infoAccountsClient() {
        ResponseEntity<UserDtoResponse> resultInsert = template.postForEntity("http://localhost:8080/api/clients", clientDto, UserDtoResponse.class);
        String cookieInsert = resultInsert.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookieInsert);

        ResponseEntity<UserDtoResponse> resultInfo = template.exchange("http://localhost:8080/api/accounts", HttpMethod.GET, new HttpEntity<String>(headers), UserDtoResponse.class);
        assertEquals(clientDto.getFirstName(), resultInfo.getBody().getFirstName());
        assertEquals(clientDto.getLastName(), resultInfo.getBody().getLastName());
        assertEquals(clientDto.getPatronymic(), resultInfo.getBody().getPatronymic());
        assertEquals(clientDto.getEmail(), resultInfo.getBody().getEmail());
        assertEquals(clientDto.getAddress(), resultInfo.getBody().getAddress());
        assertEquals(clientDto.getPhone(), resultInfo.getBody().getPhone());
        assertNotEquals(0, resultInfo.getBody().getId());
        assertEquals(200, resultInfo.getStatusCode().value());
    }

    @Test
    public void infoClient() {
        ResponseEntity<UserDtoResponse> resultInsertClient = template.postForEntity("http://localhost:8080/api/clients", clientDto, UserDtoResponse.class);
        String cookieClient = resultInsertClient.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headersClient = new HttpHeaders();
        headersClient.add("Cookie", cookieClient);

        ResponseEntity<UserDtoResponse> resultInsertAdmin = template.postForEntity("http://localhost:8080/api/admins", adminDto, UserDtoResponse.class);
        String cookieAdmin = resultInsertAdmin.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headersAdmin = new HttpHeaders();
        headersAdmin.add("Cookie", cookieAdmin);

        ResponseEntity<List<UserDtoResponse>> resultInfoWithAdmin = template.exchange("http://localhost:8080/api/clients", HttpMethod.GET, new HttpEntity<String>(headersAdmin), new ParameterizedTypeReference<List<UserDtoResponse>>() {
        });

        assertEquals(clientDto.getFirstName(), resultInfoWithAdmin.getBody().get(0).getFirstName());
        assertEquals(clientDto.getLastName(), resultInfoWithAdmin.getBody().get(0).getLastName());
        assertEquals(clientDto.getPatronymic(), resultInfoWithAdmin.getBody().get(0).getPatronymic());
        assertEquals(clientDto.getEmail(), resultInfoWithAdmin.getBody().get(0).getEmail());
        assertEquals(clientDto.getAddress(), resultInfoWithAdmin.getBody().get(0).getAddress());
        assertEquals(clientDto.getPhone(), resultInfoWithAdmin.getBody().get(0).getPhone());
        assertEquals("CLIENT", resultInfoWithAdmin.getBody().get(0).getUserType());
        assertEquals(1, resultInfoWithAdmin.getBody().size());
        assertNotEquals(0, resultInfoWithAdmin.getBody().get(0).getId());
        assertEquals(200, resultInfoWithAdmin.getStatusCode().value());

        try {
            template.exchange("http://localhost:8080/api/clients", HttpMethod.GET, new HttpEntity<String>(headersClient), new ParameterizedTypeReference<List<UserDtoResponse>>() {
            });
        } catch (HttpClientErrorException exc) {
            assertEquals(400, exc.getRawStatusCode());
        }
    }


    @Test
    public void editAdminProfile() {
        ResponseEntity<UserDtoResponse> resultInsertAdmin = template.postForEntity("http://localhost:8080/api/admins", adminDto, UserDtoResponse.class);
        String cookieAdmin = resultInsertAdmin.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headersAdmin = new HttpHeaders();
        headersAdmin.add("Cookie", cookieAdmin);

        AdminDtoWithoutLogin adminDto = new AdminDtoWithoutLogin();
        adminDto.setFirstName("НОВОЕ");
        adminDto.setLastName("НОВОЕ");
        adminDto.setPatronymic("НОВОЕ");
        adminDto.setNewPassword("НОВОЕ");
        adminDto.setPosition("НОВОЕ");
        adminDto.setOldPassword("Пароль");

        ResponseEntity<UserDtoResponse> result = template.exchange("http://localhost:8080/api/admins", HttpMethod.PUT, new HttpEntity<Object>(adminDto, headersAdmin), UserDtoResponse.class);

        assertEquals(adminDto.getFirstName(), result.getBody().getFirstName());
        assertEquals(adminDto.getLastName(), result.getBody().getLastName());
        assertEquals(adminDto.getPatronymic(), result.getBody().getPatronymic());
        assertEquals(adminDto.getPosition(), result.getBody().getPosition());
        assertNotEquals(0, result.getBody().getId());
        assertEquals(200, result.getStatusCode().value());
        assertNotEquals(adminDto.getFirstName(), resultInsertAdmin.getBody().getFirstName());
    }

    @Test
    public void editClientProfile() {
        ResponseEntity<UserDtoResponse> resultInsertClient = template.postForEntity("http://localhost:8080/api/clients", clientDto, UserDtoResponse.class);
        String cookieClient = resultInsertClient.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headersClient = new HttpHeaders();
        headersClient.add("Cookie", cookieClient);

        ClientDtoWithoutLogin clientDto = new ClientDtoWithoutLogin();
        clientDto.setFirstName("НОВОЕ");
        clientDto.setLastName("НОВОЕ");
        clientDto.setPatronymic("НОВОЕ");
        clientDto.setNewPassword("НОВОЕ");
        clientDto.setAddress("НОВОЕ");
        clientDto.setEmail("321.com");
        clientDto.setPhone("8912987654");
        clientDto.setOldPassword("ПарольКлиент");

        ResponseEntity<UserDtoResponse> result = template.exchange("http://localhost:8080/api/clients", HttpMethod.PUT, new HttpEntity<Object>(clientDto, headersClient), UserDtoResponse.class);

        assertEquals(clientDto.getFirstName(), result.getBody().getFirstName());
        assertEquals(clientDto.getLastName(), result.getBody().getLastName());
        assertEquals(clientDto.getPatronymic(), result.getBody().getPatronymic());
        assertEquals(clientDto.getAddress(), result.getBody().getAddress());
        assertEquals(clientDto.getEmail(), result.getBody().getEmail());
        assertEquals(clientDto.getPhone(), result.getBody().getPhone());
        assertNotEquals(0, result.getBody().getId());
        assertEquals(200, result.getStatusCode().value());
        assertNotEquals(clientDto.getFirstName(), resultInsertClient.getBody().getFirstName());
    }

    @Test
    public void addCategoryOrSubcategory() {
        ResponseEntity<UserDtoResponse> resultInsertAdmin = template.postForEntity("http://localhost:8080/api/admins", adminDto, UserDtoResponse.class);
        String cookieAdmin = resultInsertAdmin.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headersAdmin = new HttpHeaders();
        headersAdmin.add("Cookie", cookieAdmin);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("К1");
        ResponseEntity<CategoryDtoResponse> resultCategory = template.exchange("http://localhost:8080/api/categories", HttpMethod.POST, new HttpEntity<Object>(categoryDto, headersAdmin), CategoryDtoResponse.class);

        assertEquals(categoryDto.getName(), resultCategory.getBody().getName());
        assertNotEquals(0, resultCategory.getBody().getId());
        assertNull(resultCategory.getBody().getParentId());
        assertNull(resultCategory.getBody().getParentName());

        CategoryDto subcategoryDto = new CategoryDto();
        subcategoryDto.setName("S1");
        subcategoryDto.setParentId(resultCategory.getBody().getId());
        ResponseEntity<CategoryDtoResponse> resultSubcategory = template.exchange("http://localhost:8080/api/categories", HttpMethod.POST, new HttpEntity<Object>(subcategoryDto, headersAdmin), CategoryDtoResponse.class);

        assertEquals(subcategoryDto.getName(), resultSubcategory.getBody().getName());
        assertNotEquals(0, resultSubcategory.getBody().getId());
        assertEquals(resultCategory.getBody().getId(), Integer.parseInt(resultSubcategory.getBody().getParentId()));
        assertEquals(resultCategory.getBody().getName(), resultSubcategory.getBody().getParentName());
    }

    @Test
    public void getAllCategoryOrSubcategory() {
        ResponseEntity<UserDtoResponse> resultInsertAdmin = template.postForEntity("http://localhost:8080/api/admins", adminDto, UserDtoResponse.class);
        String cookieAdmin = resultInsertAdmin.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headersAdmin = new HttpHeaders();
        headersAdmin.add("Cookie", cookieAdmin);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("К1");
        ResponseEntity<CategoryDtoResponse> resultPostCategory = template.exchange("http://localhost:8080/api/categories", HttpMethod.POST, new HttpEntity<Object>(categoryDto, headersAdmin), CategoryDtoResponse.class);

        CategoryDto subcategoryDto = new CategoryDto();
        subcategoryDto.setName("S1");
        subcategoryDto.setParentId(resultPostCategory.getBody().getId());
        template.exchange("http://localhost:8080/api/categories", HttpMethod.POST, new HttpEntity<Object>(subcategoryDto, headersAdmin), CategoryDtoResponse.class);

        ResponseEntity<List<CategoryDtoResponse>> resultAllCategory = template.exchange("http://localhost:8080/api/categories", HttpMethod.GET, new HttpEntity<>(headersAdmin), new ParameterizedTypeReference<List<CategoryDtoResponse>>() {
        });

        assertEquals(categoryDto.getName(), resultAllCategory.getBody().get(0).getName());
        assertEquals(subcategoryDto.getName(), resultAllCategory.getBody().get(1).getName());
        assertEquals(subcategoryDto.getParentId(), Integer.parseInt(resultAllCategory.getBody().get(1).getParentId()));
        assertEquals(2, resultAllCategory.getBody().size());
    }
}