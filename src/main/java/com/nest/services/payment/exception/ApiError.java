package com.nest.services.payment.exception;

public enum ApiError {

    INVALID_REQUEST(400, "Request.Invalid", "The request is invalid"),
    INTERNAL_ERROR(500, "InternalServerError", "Internal Server Error"),
    PRECONDITION_FAILED(412, "BusinessRulesViolation", "The business rules is being violated as part of this transaction"),
    RESOURCE_NOT_FOUND(404, "Resource NotFound", "Requested resource is not found");

    private int code;
    private String errorCode;
    private String message;

    ApiError(int code, String errorCode, String message) {
        this.code = code;
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
