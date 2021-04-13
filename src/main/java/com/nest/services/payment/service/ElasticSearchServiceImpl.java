package com.nest.services.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nest.services.payment.representation.ApiException;
import com.nest.services.payment.representation.PaymentDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);

    @Autowired
    private PaymentDb paymentDb;

    @Value("${elasticsearch.request.url}")
    private String elasticsearchSequestUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String refreshElasticSearch() {
        LOGGER.info("Processing request to refresh ElasticSearch");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request;
        try {
            request = new HttpEntity<>(objectMapper.writeValueAsString(paymentDb), headers);
        } catch (JsonProcessingException e) {
            throw new ApiException("Failed to create payload data for ElasticSearch", e);
        }

        String elasticSearchJsonRequest = restTemplate.postForObject(elasticsearchSequestUrl, request, String.class);
        JsonNode root;
        try {
            root = objectMapper.readTree(elasticSearchJsonRequest);
        } catch (JsonProcessingException e) {
            throw new ApiException("Failed to upload data to ElasticSearch", e);
        }
        LOGGER.info("Data has been uploaded to ElasticSearch: {}", root);

        return "success";
    }
}
