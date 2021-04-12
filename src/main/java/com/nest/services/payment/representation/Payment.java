package com.nest.services.payment.representation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {

    private Account sourceAccount;

    private List<Transaction> transactions;

    private BigDecimal availableBalance;

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public static final class PaymentBuilder {
        private Account sourceAccount;
        private List<Transaction> transactions;
        private BigDecimal availableBalance;

        private PaymentBuilder() {
        }

        public static PaymentBuilder aPayment() {
            return new PaymentBuilder();
        }

        public PaymentBuilder withSourceAccount(Account sourceAccount) {
            this.sourceAccount = sourceAccount;
            return this;
        }

        public PaymentBuilder withTransactions(List<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        public PaymentBuilder withAvailableBalance(BigDecimal availableBalance) {
            this.availableBalance = availableBalance;
            return this;
        }

        public Payment build() {
            Payment payment = new Payment();
            payment.transactions = this.transactions;
            payment.availableBalance = this.availableBalance;
            payment.sourceAccount = this.sourceAccount;
            return payment;
        }
    }

}
