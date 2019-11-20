package net.thumbtack.onlineshop.dto.response;

import net.thumbtack.onlineshop.exceptions.ApiError;

import java.util.List;

public class ResponseError {

    private List<ApiError> errors;

    public ResponseError(List<ApiError> errors) {
        this.errors = errors;
    }

    public List<ApiError> getErrors() {
        return errors;
    }
}
