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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PaymentDaoImpl implements PaymentDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentDaoImpl.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private PaymentDb paymentDb;

    @Value("${payment.json.path}")
    private String dataPath;

    @Override
    public void savePayment(PaymentRequest paymentRequest) {
        LOGGER.debug("Processing payment request to store");

        int debitAccountIndex = searchPaymentForAccount(paymentRequest.getSourceAccount());

        List<Transaction> transactions = new ArrayList<>();
        BigDecimal availableBalance;
        Payment payment;
        if (debitAccountIndex == -1) {
            LOGGER.debug("Requested debit account [{}-{}] does not exist so initially loading the balance before making the payment",
                            paymentRequest.getSourceAccount().getSortCode(), paymentRequest.getSourceAccount().getAccountNumber());
            availableBalance = new BigDecimal("1000000");
            transactions.add(buildTransaction(paymentRequest, availableBalance));

            payment = Payment.PaymentBuilder.aPayment()
                            .withAvailableBalance(availableBalance.subtract(paymentRequest.getAmount()))
                            .withSourceAccount(paymentRequest.getSourceAccount())
                            .withTransactions(transactions)
                            .build();
        } else {
            LOGGER.debug("Requested debit account [{}-{}] already exists", paymentRequest.getSourceAccount().getSortCode(), paymentRequest.getSourceAccount().getAccountNumber());
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
        LOGGER.debug("Checking balance before making the payment");
        if (balance.subtract(amount).compareTo(BigDecimal.ZERO) == -1) {
            throw new PaymentException("Insufficient Funds");
        }
        LOGGER.info("Balance check has been performed successfully");
    }

    private synchronized void savePaymentDetails(PaymentDb paymentDb) {
        LOGGER.debug("Saving the payment details into JSON file");
        try {
            MAPPER.writeValue(new File(dataPath), paymentDb);
        } catch (IOException e) {
            throw new ApiException("Failed to write data to file: " + e.getMessage());
        }
        LOGGER.debug("Payment details have been successfully stored into JSON file");
    }

    private int searchPaymentForAccount(Account debitAccount) {
        LOGGER.debug("Searching payment details for account into JSON file");
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
        MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

}
