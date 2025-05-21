package com.nadeex.stock_trading_server.service;

import com.nadeex.grpc.*;
import com.nadeex.stock_trading_server.entity.Stock;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import com.nadeex.stock_trading_server.repository.StockRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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

    @Override
    public void subscribeStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        try {
            String stockSymbol = request.getStockSymbol();

            for (int i = 0; i < 10; i++) {
                Stock stock = stockRepository.findByStockSymbol(stockSymbol);

                StockResponse stockResponse = StockResponse.newBuilder()
                        .setStockSymbol(stock.getStockSymbol())
                        .setPrice(new Random().nextDouble(200))
                        .setTimestamp(Instant.now().toString())
                        .build();
                responseObserver.onNext(stockResponse);
                TimeUnit.SECONDS.sleep(1);
            }
            responseObserver.onCompleted();
        }catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public StreamObserver<StockOrder> bulkStockOrder(StreamObserver<OrderSummary> responseObserver) {


        return new StreamObserver<>() {
            private int totalOrders = 0;
            private double totalAmount = 0.0;
            private int successCount = 0;

            @Override
            public void onNext(StockOrder stockOrder) {
                totalOrders++;
                totalAmount += stockOrder.getQuantity() * stockOrder.getPrice();
                successCount++;
                System.out.println("Received order: " + stockOrder.getStockSymbol() + ", Quantity: " + stockOrder.getQuantity() + ", Price: " + stockOrder.getPrice());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                var orderSummary = OrderSummary.newBuilder()
                        .setTotalOrders(totalOrders)
                        .setTotalAmount(totalAmount)
                        .setSuccessCount(successCount)
                        .build();
                responseObserver.onNext(orderSummary);
                responseObserver.onCompleted();
                System.out.println("Bulk order completed. Total orders: " + totalOrders + ", Total amount: " + totalAmount + ", Successful orders: " + successCount);
            }
        };
    }

    @Override
    public StreamObserver<StockOrder> liveTrading(StreamObserver<TradeStatus> responseObserver) {

        return new StreamObserver<>() {

            @Override
            public void onNext(StockOrder stockOrder) {
                System.out.println("Received live order: " + stockOrder.getStockSymbol() + ", Quantity: " + stockOrder.getQuantity() + ", Price: " + stockOrder.getPrice());

                String status = "EXECUTED";
                String message = "Order executed successfully";
                if (stockOrder.getQuantity() <= 0) {
                    status = "FAILED";
                    message = "Order execution failed";
                }

                TradeStatus tradeStatus = TradeStatus.newBuilder()
                        .setStatus(status)
                        .setMessage(message)
                        .setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                        .build();


                responseObserver.onNext(tradeStatus);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
                System.out.println("Live trading completed.");
            }
        };
    }
}
