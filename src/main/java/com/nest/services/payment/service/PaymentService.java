package com.nest.services.payment.service;

import com.nest.services.payment.representation.PaymentRequest;
import com.nest.services.payment.representation.PaymentResponse;

public interface PaymentService {

    PaymentResponse makePayment(PaymentRequest paymentRequest);
}
