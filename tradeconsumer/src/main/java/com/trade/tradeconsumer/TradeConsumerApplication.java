package com.trade.tradeconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TradeConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeConsumerApplication.class, args);
    }
}
