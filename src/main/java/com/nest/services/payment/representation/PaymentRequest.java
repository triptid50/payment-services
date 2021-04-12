package com.nest.services.payment.representation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRequest {

    private Account sourceAccount;

    private Account beneficiary;

    private BigDecimal amount;

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public Account getBeneficiary() {
        return beneficiary;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public static final class PaymentRequestBuilder {
        private Account sourceAccount;
        private Account beneficiary;
        private BigDecimal amount;

        private PaymentRequestBuilder() {
        }

        public static PaymentRequestBuilder aPaymentRequest() {
            return new PaymentRequestBuilder();
        }

        public PaymentRequestBuilder withSourceAccount(Account sourceAccount) {
            this.sourceAccount = sourceAccount;
            return this;
        }

        public PaymentRequestBuilder withBeneficiary(Account beneficiary) {
            this.beneficiary = beneficiary;
            return this;
        }

        public PaymentRequestBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public PaymentRequest build() {
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.beneficiary = this.beneficiary;
            paymentRequest.amount = this.amount;
            paymentRequest.sourceAccount = this.sourceAccount;
            return paymentRequest;
        }
    }
}
