package net.thumbtack.onlineshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.daoImpl.CategoryDaoImpl;
import net.thumbtack.onlineshop.dto.request.CategoryDto;
import net.thumbtack.onlineshop.dto.response.CategoryDtoResponse;
import net.thumbtack.onlineshop.exceptions.ErrorCode;
import net.thumbtack.onlineshop.exceptions.ServiceException;
import net.thumbtack.onlineshop.exceptions.ApiError;
import net.thumbtack.onlineshop.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CategoryService {

    private ObjectMapper objectMapper;
    private CategoryDaoImpl categoryDao;
    private CategoryDtoResponse categoryDtoResponse;
    private Category category;

    @Autowired
    public CategoryService(ObjectMapper objectMapper, CategoryDaoImpl categoryDao, CategoryDtoResponse categoryDtoResponse, Category category) {
        this.objectMapper = objectMapper;
        this.categoryDao = categoryDao;
        this.categoryDtoResponse = categoryDtoResponse;
        this.category = category;
    }

    public CategoryDtoResponse addCategory(CategoryDto categoryDto) throws ServiceException {
        List<ApiError> apiErrorList = new ArrayList<>();
        category = objectMapper.convertValue(categoryDto, Category.class);
        if (categoryDto.getParentId() != 0) {
            category.setParent(categoryDao.getCategory(String.valueOf(categoryDto.getParentId())));
            if (category.getParent() == null) {
                ApiError apiError = new ApiError(ErrorCode.INVALID_PARENT_ID.name(), "parentId", ErrorCode.INVALID_PARENT_ID.getErrorString());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
            category = categoryDao.addCategory(category);
            categoryDtoResponse = objectMapper.convertValue(category, CategoryDtoResponse.class);
            categoryDtoResponse.setParentName(category.getParent().getName());
            categoryDtoResponse.setParentId(String.valueOf(category.getParent().getId()));
        } else {
            category = categoryDao.addCategory(category);
            categoryDtoResponse = objectMapper.convertValue(category, CategoryDtoResponse.class);
        }
        return categoryDtoResponse;
    }

    public CategoryDtoResponse getCategory(String id) throws ServiceException {
        category = categoryDao.getCategory(id);
        categoryDtoResponse = objectMapper.convertValue(category, CategoryDtoResponse.class);
        if (category.getParent() != null) {
            categoryDtoResponse.setParentId(String.valueOf(category.getParent().getId()));
            categoryDtoResponse.setParentName(category.getParent().getName());
        }
        return categoryDtoResponse;
    }

    public CategoryDtoResponse editCategory(CategoryDto categoryDto) throws ServiceException {
        List<ApiError> apiErrorList = new ArrayList<>();
        Category newParent;
        Category categoryBeforeChange;
        category = objectMapper.convertValue(categoryDto, Category.class);
        categoryBeforeChange = categoryDao.getCategory(String.valueOf(category.getId()));
        if (category.getName() != null) {
            category = categoryDao.editCategoryName(category);
        } else {
            category.setName(categoryBeforeChange.getName());
        }
        if (categoryBeforeChange.getParent() != null) {
            Category oldParent = categoryDao.getCategory(String.valueOf(categoryBeforeChange.getParent().getId()));
            newParent = categoryDao.getCategory(String.valueOf(categoryDto.getParentId()));
            if (categoryDto.getParentId() != 0) {
                category.setParent(newParent);
                if (category.getParent() == null && newParent.getParent() != null) {
                    ApiError apiError = new ApiError(ErrorCode.INVALID_PARENT_ID.name(), "parentId", ErrorCode.INVALID_PARENT_ID.getErrorString());
                    apiErrorList.add(apiError);
                    throw new ServiceException(apiErrorList);
                }
            }
            if (newParent.getParent() == null && oldParent.getParent() == null) {
                if (category.getParent() != null) {
                    category = categoryDao.editCategoryParent(category);
                } else {
                    category.setParent(categoryDao.getParentById(category));
                }
            } else {
                ApiError apiError = new ApiError(ErrorCode.CATEGORY_NOT_PARENT.name(), "parentId", ErrorCode.CATEGORY_NOT_PARENT.getErrorString());
                apiErrorList.add(apiError);
                throw new ServiceException(apiErrorList);
            }
        } else {
            category.setParent(null);
        }
        categoryDtoResponse = objectMapper.convertValue(category, CategoryDtoResponse.class);
        if (category.getParent() != null) {
            categoryDtoResponse.setParentName(category.getParent().getName());
            categoryDtoResponse.setParentId(String.valueOf(category.getParent().getId()));
        }
        return categoryDtoResponse;
    }

    public CategoryDtoResponse deleteCategory(String id) throws ServiceException {
        categoryDao.deleteCategory(id);
        return new CategoryDtoResponse();
    }

    public List<CategoryDtoResponse> getAllCategory() throws ServiceException {
        List<CategoryDtoResponse> sortCategories = new ArrayList<>();
        List<Category> categories = categoryDao.getAllCategory();
        categories.sort(Comparator.comparing(Category::getName));
        for (Category category : categories) {
            if (category.getParent() == null) {
                sortCategories.add(objectMapper.convertValue(category, CategoryDtoResponse.class));
                for (Category subcategory : categories) {
                    if (subcategory.getParent() != null && subcategory.getParent().getId() == category.getId()) {
                        categoryDtoResponse = objectMapper.convertValue(subcategory, CategoryDtoResponse.class);
                        categoryDtoResponse.setParentName(subcategory.getParent().getName());
                        categoryDtoResponse.setParentId(String.valueOf(subcategory.getParent().getId()));
                        sortCategories.add(categoryDtoResponse);
                    }
                }
            }
        }
        return sortCategories;
    }
}
