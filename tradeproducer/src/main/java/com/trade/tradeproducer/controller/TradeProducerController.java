package com.trade.tradeproducer.controller;


import com.trade.model.Trade;
import com.trade.tradeproducer.service.TradeProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trades")
public class TradeProducerController {

    private static final Logger logger = LoggerFactory.getLogger(TradeProducerController.class);

    @Autowired
    private TradeProducerService producerService;

    @PostMapping
    public ResponseEntity<String> createTrade(@RequestBody Trade trade) {
        logger.info("Received trade creation request for trade ID: {}", trade.getTradeId());

        if (trade.getTradeId() == null || trade.getTradeId().isEmpty()) {
            logger.error("Trade ID is required");
            return ResponseEntity.badRequest().body("Trade ID is required");
        }


        try {
            producerService.sendTrade(trade);
                logger.info("Successfully processed trade with ID: {}", trade.getTradeId());
                return ResponseEntity.ok("Trade processed successfully");
        }
            catch (Exception e) {
                    logger.error("Error processing trade with ID: {}", trade.getTradeId(), e);
                return ResponseEntity.internalServerError().body("Error processing trade");
        }


        }
}


/*
    private final TradeProducerService producerService;

    public TradeProducerController(TradeProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestParam String topic,
                                          @RequestParam String message) {
        producerService.sendMessage(topic, message);
        return ResponseEntity.ok("Message sent to topic: " + topic);
    }

    */

