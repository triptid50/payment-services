package com.nest.services.payment.dao;

import com.nest.services.payment.representation.PaymentRequest;

public interface PaymentDao {

    void savePayment(PaymentRequest paymentRequest);
}
