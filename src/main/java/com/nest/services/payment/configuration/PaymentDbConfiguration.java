package com.nest.services.payment.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nest.services.payment.exception.ApiException;
import com.nest.services.payment.representation.PaymentDb;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class PaymentDbConfiguration {

    @Bean
    public PaymentDb paymentDb() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(new File("src/main/resources/db/payment-db.json"), PaymentDb.class);
        } catch (IOException e) {
            throw new ApiException("Failed to load the payment database details: " + e.getMessage());
        }
    }

}
