package com.trade.tradeconsumer.repository;

import com.trade.tradeconsumer.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<TradeEntity, String> {
    Optional<TradeEntity> findByTradeId(String tradeId);
}