package com.nest.services.payment.service;

import com.nest.services.payment.dao.PaymentDao;
import com.nest.services.payment.representation.PaymentRequest;
import com.nest.services.payment.representation.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentDao paymentDao;

    @Override
    public PaymentResponse makePayment(PaymentRequest paymentRequest) {
        LOGGER.info("Processing payment request");
        paymentDao.savePayment(paymentRequest);

        LOGGER.info("Payment has been Processed successfully");

        return PaymentResponse.PaymentResponseBuilder.aPaymentResponse()
                        .withStatus("success")
                        .withStatusCode("0000")
                        .withStatusDescription("Payment request successful")
                        .build();
    }

}
