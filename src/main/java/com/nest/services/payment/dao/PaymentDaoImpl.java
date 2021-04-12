package com.nest.services.payment.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nest.services.payment.exception.ApiException;
import com.nest.services.payment.exception.PaymentException;
import com.nest.services.payment.representation.Account;
import com.nest.services.payment.representation.Payment;
import com.nest.services.payment.representation.PaymentDb;
import com.nest.services.payment.representation.PaymentRequest;
import com.nest.services.payment.representation.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PaymentDaoImpl implements PaymentDao {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PaymentDb paymentDb;

    @Override
    public void savePayment(PaymentRequest paymentRequest) {

        int debitAccountIndex = searchPaymentForAccount(paymentRequest.getSourceAccount());

        List<Transaction> transactions = new ArrayList<>();
        BigDecimal availableBalance;
        Payment payment;
        if (debitAccountIndex == -1) {
            availableBalance = new BigDecimal("1000000");
            transactions.add(buildTransaction(paymentRequest, availableBalance));

            payment = Payment.PaymentBuilder.aPayment()
                            .withAvailableBalance(availableBalance.subtract(paymentRequest.getAmount()))
                            .withSourceAccount(paymentRequest.getSourceAccount())
                            .withTransactions(transactions)
                            .build();
        } else {
            payment = paymentDb.getPayments().get(debitAccountIndex);
            availableBalance = payment.getAvailableBalance();
            checkBalanceBeforePayment(paymentRequest.getAmount(), availableBalance);

            payment.getTransactions().add(buildTransaction(paymentRequest, availableBalance));
            payment.setAvailableBalance(availableBalance.subtract(paymentRequest.getAmount()));
        }

        if (debitAccountIndex != -1) {
            paymentDb.getPayments().remove(debitAccountIndex);
        }

        paymentDb.getPayments().add(payment);

        savePaymentDetails(paymentDb);
    }

    private void checkBalanceBeforePayment(BigDecimal amount, BigDecimal balance) {
        if (balance.subtract(amount).compareTo(BigDecimal.ZERO) == -1) {
            throw new PaymentException("Insufficient Funds");
        }
    }

    private synchronized void savePaymentDetails(PaymentDb paymentDb) {
        try {
            objectMapper.writeValue(new File("src/main/resources/db/payment-db.json"), paymentDb);
        } catch (IOException e) {
            throw new ApiException("Failed to write data to file: " + e.getMessage());
        }
    }

    private int searchPaymentForAccount(Account debitAccount) {
        AtomicInteger i = new AtomicInteger();
        return paymentDb.getPayments().stream()
                        .peek(v -> i.incrementAndGet())
                        .anyMatch(payment -> payment.getSourceAccount().getAccountNumber().equals(debitAccount.getAccountNumber()) && payment.getSourceAccount().getSortCode()
                                        .equals(debitAccount.getSortCode())) ? i.get() - 1 : -1;
    }

    private Transaction buildTransaction(PaymentRequest paymentRequest, BigDecimal availableBalance) {
        return Transaction.TransactionBuilder.aTransaction()
                        .withAmount(paymentRequest.getAmount())
                        .withBalance(availableBalance.subtract(paymentRequest.getAmount()))
                        .withBeneficiary(paymentRequest.getBeneficiary())
                        .build();
    }

    static {
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

}
