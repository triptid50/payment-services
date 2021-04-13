package com.nest.services.payment.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nest.services.payment.exception.ApiException;
import com.nest.services.payment.representation.PaymentDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class PaymentDbConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentDbConfiguration.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${payment.json.path}")
    private String dataPath;

    @Bean
    public PaymentDb paymentDb() {
        LOGGER.info("Loading payment details from database");
        try {
            PaymentDb paymentDb = MAPPER.readValue(new File(dataPath), PaymentDb.class);
            if (paymentDb == null) {
                paymentDb = new PaymentDb();
            }
            return paymentDb;
        } catch (IOException e) {
            throw new ApiException("Failed to load the payment database details: " + e.getMessage());
        }
    }

}
