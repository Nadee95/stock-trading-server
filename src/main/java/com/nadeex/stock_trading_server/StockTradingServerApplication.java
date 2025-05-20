package com.nadeex.stock_trading_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

@SpringBootApplication
public class StockTradingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockTradingServerApplication.class, args);
	}

}
