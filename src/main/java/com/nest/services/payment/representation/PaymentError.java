package com.nest.services.payment.representation;

public class PaymentError {

    private String id;
    private String message;

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public static final class PaymentErrorBuilder {
        private String id;
        private String message;

        private PaymentErrorBuilder() {
        }

        public static PaymentErrorBuilder aPaymentError() {
            return new PaymentErrorBuilder();
        }

        public PaymentErrorBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public PaymentErrorBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public PaymentError build() {
            PaymentError paymentError = new PaymentError();
            paymentError.id = this.id;
            paymentError.message = this.message;
            return paymentError;
        }
    }
}

