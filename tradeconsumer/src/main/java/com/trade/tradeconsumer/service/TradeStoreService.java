package com.trade.tradeconsumer.service;


import com.trade.tradeconsumer.entity.TradeEntity;
import com.trade.model.Trade;
import com.trade.tradeconsumer.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j

/**
 * This TradeStoreService Class listens to Kafka topic- [tradetopic] on which Trades are published by TradeProducer.
 * It stores incoming trade based on certain validations
 */

public class TradeStoreService {

    @Autowired
    private TradeRepository tradeRepository;

    public void processTrade(Trade trade) {
        log.info("###Processing trade: {}", trade);
        validateTrade(trade);
        saveTrade(trade);
    }


    /*** Validations on Incoming Trade before storing the Trade
     *
     * 1.	If a trade with a lower version is received during transmission, the store will reject it and generate an exception. Trades with the same version will replace the current record.
     * 2.	The store must reject any trade that has a maturity date earlier than today's date.
     * 3.	When a trade's maturity date is surpassed, the store should automatically mark the trade as expired.
     *
     */
    private void validateTrade(Trade trade) {
        // Validate version
        tradeRepository.findByTradeId(trade.getTradeId())
                .ifPresent(existingTrade -> {
                    if (trade.getVersion() < existingTrade.getVersion()) {
                        throw new IllegalArgumentException(
                                "Trade version is lower than existing version");
                    }
                });

        // Validate maturity date
        if (trade.getMaturityDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Trade maturity date is before current date");
        }
    }

    private void saveTrade(Trade trade) {
        log.info("Saving trade: {}", trade);
        TradeEntity tradeEntity = convertToEntity(trade);

        tradeRepository.save(tradeEntity);
    }


    private TradeEntity convertToEntity(Trade tradeMessage) {
        TradeEntity trade = new TradeEntity();
        // Manual mapping
        trade.setTradeId(tradeMessage.getTradeId());
        trade.setVersion(tradeMessage.getVersion());
        trade.setCounterPartyId(tradeMessage.getCounterPartyId());
        trade.setBookId(tradeMessage.getBookId());
        trade.setMaturityDate(tradeMessage.getMaturityDate());
        trade.setCreatedDate(tradeMessage.getCreatedDate());
        trade.setExpired(tradeMessage.isExpired());

        return trade;
    }


    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    public void updateExpiredTrades() {

        LocalDate today = LocalDate.now();
        log.info("### Scheduler Updating expired trades"+ today);

        List<TradeEntity> trades = tradeRepository.findAll();
        trades.stream()
                .filter(trade -> !trade.isExpired() &&
                        trade.getMaturityDate().isBefore(today))
                .forEach(trade -> {
                    trade.setExpired(true);
                    tradeRepository.save(trade);
                });

    }
}
