package com.spacezant.pricing.kafka;

import com.spacezant.pricing.dto.product.VariantPricingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class PricingKafkaProducer {
//
//    private final KafkaTemplate<String, VariantPricingEvent> kafkaTemplate;
//
//    private static final String TOPIC = "varient-price";
//
//    public void sendPricing(VariantPricingEvent event) {
//        log.info("🚀 Sending to Kafka: {}", event);
//        kafkaTemplate.send(TOPIC, event);
//    }
//}