package com.nadeex.stock_trading_server.service;

import com.nadeex.stocktrading.StockRequest;
import com.nadeex.stocktrading.StockResponse;
import com.nadeex.stocktrading.StockTradingServiceGrpc;
import com.nadeex.stock_trading_server.entity.Stock;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import com.nadeex.stock_trading_server.repository.StockRepository;

@GrpcService
public class StockTradingServiceImpl extends StockTradingServiceGrpc.StockTradingServiceImplBase {

    private final StockRepository stockRepository;

    public StockTradingServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void getStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {

        try {
            String stockSymbol = request.getStockSymbol();

            Stock stock = stockRepository.findByStockSymbol(stockSymbol);

            StockResponse stockResponse = StockResponse.newBuilder()
                    .setStockSymbol(stock.getStockSymbol())
                    .setPrice(stock.getPrice())
                    .setTimestamp(stock.getLastUpdated())
                    .build();
            responseObserver.onNext(stockResponse);
            responseObserver.onCompleted();
        }catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal error: " + e.getMessage())
                    .asRuntimeException());

        }
    }
}
