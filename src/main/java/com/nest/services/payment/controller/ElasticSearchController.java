package com.nest.services.payment.controller;

import com.nest.services.payment.service.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ElasticSearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchController.class);

    @Autowired
    private ElasticSearchService elasticSearchService;

    @PostMapping("/payment/refresh")
    public ResponseEntity<String> makePayment() {
        LOGGER.info("Refreshing elastic search index with recent payment details");
        return ResponseEntity.ok(elasticSearchService.refreshElasticSearch());
    }

}
