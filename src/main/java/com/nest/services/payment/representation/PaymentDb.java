package com.nest.services.payment.representation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDb {

    List<Payment> payments;

    public List<Payment> getPayments() {
        if(payments == null) {
            return new ArrayList<>();
        }
        return payments;
    }

    public void add(Payment payment) {
        payments.add(payment);
    }

    public static final class PaymentDbBuilder {
        List<Payment> payments;

        private PaymentDbBuilder() {
        }

        public static PaymentDbBuilder aPaymentDb() {
            return new PaymentDbBuilder();
        }

        public PaymentDbBuilder withPayments(List<Payment> payments) {
            this.payments = payments;
            return this;
        }

        public PaymentDb build() {
            PaymentDb paymentDb = new PaymentDb();
            paymentDb.payments = this.payments;
            return paymentDb;
        }
    }
}
