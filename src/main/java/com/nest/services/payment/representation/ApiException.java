package com.nest.services.payment.representation;

import com.nest.services.payment.exception.ApiError;

public class ApiException extends RuntimeException {

    private final ApiError apiError;

    public ApiException(String msg, Throwable th) {
        super(msg);
        this.apiError = ApiError.INTERNAL_ERROR;
    }

    public ApiException(ApiError apiError, String msg) {
        super(msg);
        this.apiError = apiError;
    }

    public ApiError getApiError() {
        return apiError;
    }

}
