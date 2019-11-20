package net.thumbtack.onlineshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.daoImpl.AdminDaoImpl;
import net.thumbtack.onlineshop.dto.request.AdminDto;
import net.thumbtack.onlineshop.dto.request.AdminDtoWithoutLogin;
import net.thumbtack.onlineshop.dto.response.PurchaseReportDtoResponse;
import net.thumbtack.onlineshop.dto.response.ReportDtoResponse;
import net.thumbtack.onlineshop.dto.response.UserDtoResponse;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.model.Admin;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final AdminDaoImpl adminDao;
    private ObjectMapper objectMapper;

    @Autowired
    public AdminService(AdminDaoImpl adminDao, ObjectMapper objectMapper) {
        this.adminDao = adminDao;
        this.objectMapper = objectMapper;
    }

    public UserDtoResponse insert(AdminDto adminDto) throws ServiceException {
        Admin admin = objectMapper.convertValue(adminDto, Admin.class);
        return objectMapper.convertValue(adminDao.insert(admin), UserDtoResponse.class);
    }

    public List<UserDtoResponse> infoClient() throws ServiceException {
        List<Client> clientList = adminDao.getClientInfo();
        List<UserDtoResponse> userDtoResponses = new LinkedList<>();
        for (Client client : clientList) {
            userDtoResponses.add(objectMapper.convertValue(client, UserDtoResponse.class));
            userDtoResponses.get(userDtoResponses.size() - 1).setUserType(client.getRole().name());
        }
        return userDtoResponses;
    }

    public UserDtoResponse editAdminProfile(AdminDtoWithoutLogin adminDto) throws ServiceException {
        Admin admin = (objectMapper.convertValue(adminDto, Admin.class));
        admin.setPassword(adminDto.getNewPassword());
        return objectMapper.convertValue(adminDao.editAdminProfile(admin), UserDtoResponse.class);
    }

    public ReportDtoResponse getReport(Integer offset, Integer limit, String results, String regularize, String criterion, List<Integer> additionallyList, List<String> date) throws ServiceException {
        Map<String, String> map = new HashMap<>();
        ReportDtoResponse reportDtoResponse = new ReportDtoResponse();
        List<PurchaseReportDtoResponse> responses = new ArrayList<>();
        String begin = null;
        String end = null;
        String additionally = null;
        LocalDateTime dateBeginning, dateEnd;

        if (additionallyList != null) {
            additionally = additionallyList.stream().map(Object::toString).collect(Collectors.joining(", "));
        }
        try {
            if (date != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                dateBeginning = LocalDateTime.parse(date.get(0), formatter);
                dateEnd = LocalDateTime.parse(date.get(1), formatter);
                begin = "'" + dateBeginning.format(formatter) + "'";
                end = "'" + dateEnd.format(formatter) + "'";
            }
        } catch (RuntimeException e) {
            begin = null;
            end = null;
        }
        if (regularize == null
                || !(regularize.equals("name")
                || regularize.equals("count")
                || regularize.equals("price")
                || regularize.equals("date_time")
                || regularize.equals("clientid")
                || regularize.equals("productid"))) {
            regularize = "date_time";
        }
        map.put("offset", String.valueOf(offset));
        map.put("limit", String.valueOf(limit));
        map.put("results", results);
        map.put("regularize", regularize);
        map.put("criterion", criterion);
        map.put("additionally", additionally);
        map.put("dateBeginning", begin);
        map.put("dateEnd", end);

        int productsSold = 0;
        int totalAmountPurchases = 0;
        for (Purchase p : adminDao.getReport(map)) {
            responses.add(objectMapper.convertValue(p, PurchaseReportDtoResponse.class));
            productsSold += p.getCount();
            totalAmountPurchases += p.getPrice();
        }
        if (results == null) {
            reportDtoResponse.setPurchases(responses);
        }
        reportDtoResponse.setResult("Total amount of purchases = " + totalAmountPurchases +
                " || Number of products sold = " + productsSold);
        return reportDtoResponse;
    }
}
