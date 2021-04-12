package com.nest.services.payment.controller;

import com.nest.services.payment.exception.PaymentException;
import com.nest.services.payment.exception.ApiError;
import com.nest.services.payment.representation.ApiException;
import com.nest.services.payment.representation.PaymentError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlingControllerAdvice.class);

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<PaymentError> onConstraintValidationException(ConstraintViolationException e) {
        LOGGER.error("Constraint violation appeared: ", e);

        PaymentError paymentError = PaymentError.PaymentErrorBuilder.aPaymentError()
                        .withId(ApiError.INVALID_REQUEST.getErrorCode())
                        .withMessage(ApiError.INVALID_REQUEST.getMessage())
                        .build();

        return new ResponseEntity<>(paymentError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    ResponseEntity<PaymentError> onApiException(ApiException apiException) {
        LOGGER.error("ApiException appeared: ", apiException);

        PaymentError paymentError = PaymentError.PaymentErrorBuilder.aPaymentError()
                        .withId(ApiError.INTERNAL_ERROR.getErrorCode())
                        .withMessage(ApiError.INTERNAL_ERROR.getMessage())
                        .build();

        return new ResponseEntity<>(paymentError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<PaymentError> onException(Exception exception) {
        LOGGER.error("Exception appeared: ", exception);

        PaymentError paymentError = PaymentError.PaymentErrorBuilder.aPaymentError()
                        .withId(ApiError.INTERNAL_ERROR.getErrorCode())
                        .withMessage(ApiError.INTERNAL_ERROR.getMessage())
                        .build();

        return new ResponseEntity<>(paymentError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PaymentException.class)
    ResponseEntity<PaymentError> onPaymentException(PaymentException exception) {
        LOGGER.error("PaymentException appeared: ", exception);

        PaymentError paymentError = PaymentError.PaymentErrorBuilder.aPaymentError()
                        .withId(ApiError.PRECONDITION_FAILED.getErrorCode())
                        .withMessage(exception.getMessage())
                        .build();

        return new ResponseEntity<>(paymentError, HttpStatus.PRECONDITION_FAILED);
    }
}
