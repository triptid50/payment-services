package com.nest.services.payment.service;

import com.nest.services.payment.dao.PaymentDao;
import com.nest.services.payment.representation.PaymentRequest;
import com.nest.services.payment.representation.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    @Override
    public PaymentResponse makePayment(PaymentRequest paymentRequest) {
        paymentDao.savePayment(paymentRequest);

        return PaymentResponse.PaymentResponseBuilder.aPaymentResponse()
                        .withStatus("success")
                        .withStatusCode("0000")
                        .withStatusDescription("Payment request successful")
                        .build();
    }

}
