package com.trade.tradeproducer.service;

import com.trade.model.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class TradeProducerService {

    private static final String TOPIC = "tradetopic";

    @Autowired
    private KafkaTemplate<String, Trade> kafkaTemplate;

    public void sendTrade(Trade trade) {
        trade.setCreatedDate(LocalDate.now());
        log.info("###Sending trade: {}", trade);
        kafkaTemplate.send(TOPIC, trade.getTradeId(), trade);
    }
}

/*

private final KafkaTemplate<String, String> kafkaTemplate;

    public TradeProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        log.info("Trade Producing message: {}", message);
        kafkaTemplate.send(topic, message);


    }

    */


