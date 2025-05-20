package com.nadeex.stock_trading_server.repository;

import com.nadeex.stock_trading_server.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Integer> {
    Stock findByStockSymbol(String stockSymbol);
}