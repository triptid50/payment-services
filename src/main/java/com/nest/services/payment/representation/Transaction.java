package com.nest.services.payment.representation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {

    private Account beneficiary;

    private BigDecimal amount;

    private BigDecimal balance;

    public Account getBeneficiary() {
        return beneficiary;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public static final class TransactionBuilder {
        private Account beneficiary;
        private BigDecimal amount;
        private BigDecimal balance;

        private TransactionBuilder() {
        }

        public static TransactionBuilder aTransaction() {
            return new TransactionBuilder();
        }

        public TransactionBuilder withBeneficiary(Account beneficiary) {
            this.beneficiary = beneficiary;
            return this;
        }

        public TransactionBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder withBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Transaction build() {
            Transaction transaction = new Transaction();
            transaction.amount = this.amount;
            transaction.beneficiary = this.beneficiary;
            transaction.balance = this.balance;
            return transaction;
        }
    }
}
