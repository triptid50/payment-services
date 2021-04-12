package com.nest.services.payment.representation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse {

    private String status;

    private String statusCode;

    private String statusDescription;

    public String getStatus() {
        return status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public static final class PaymentResponseBuilder {
        private String status;
        private String statusCode;
        private String statusDescription;

        private PaymentResponseBuilder() {
        }

        public static PaymentResponseBuilder aPaymentResponse() {
            return new PaymentResponseBuilder();
        }

        public PaymentResponseBuilder withStatus(String status) {
            this.status = status;
            return this;
        }

        public PaymentResponseBuilder withStatusCode(String statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public PaymentResponseBuilder withStatusDescription(String statusDescription) {
            this.statusDescription = statusDescription;
            return this;
        }

        public PaymentResponse build() {
            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.statusDescription = this.statusDescription;
            paymentResponse.status = this.status;
            paymentResponse.statusCode = this.statusCode;
            return paymentResponse;
        }
    }
}
