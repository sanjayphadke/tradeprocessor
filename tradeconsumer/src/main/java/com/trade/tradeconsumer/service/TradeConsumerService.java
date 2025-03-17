package com.trade.tradeconsumer.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import com.trade.model.Trade;

@Service
@Slf4j
public class TradeConsumerService {

    @Autowired
    private TradeStoreService tradeStoreService;

/*
    @KafkaListener(topics = "tradetopic", groupId = "tradestore-consumergroup")
    public void listen(String message) {
        log.info("Trade Consumer Received message#######: {}", message);
        // Add your processing logic here
    }

    // If you want to consume JSON messages:
    @KafkaListener(topics = "json-topic-name", groupId = "trade-group-id")
    public void listenJson(@Payload com.trade.consumer.model.TradeMessage message) {
        log.info("Consumer Trade Received message: {}", message);
        // Process JSON message
    }
*/

    @KafkaListener(topics = "tradetopic", groupId = "tradestore-consumergroup")
    public void consume(Trade trade) {
        log.info("#### Received trade for Consumption####: {}", trade);
        try {
            tradeStoreService.processTrade(trade);
        } catch (IllegalArgumentException e) {
            log.error("Trade validation failed: {}", e.getMessage());
        }
    }
}



