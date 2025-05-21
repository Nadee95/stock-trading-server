Here's a README.md file for your stock trading server project:

# Stock Trading Server

A gRPC-based stock trading server built with Spring Boot that provides real-time stock information and trading capabilities.

## Technologies Used

- Java
- Spring Boot
- gRPC
- MySQL
- Flyway (Database migrations)
- Maven

## Prerequisites

- JDK 17 or higher
- MySQL Server
- Maven
- grpcurl (for testing)

## Setup

1. Clone the repository:
   ```
   git clone https://github.com/Nadee95/stock-trading-server.git
   cd stock-trading-server
   ```

2. Configure environment variables:
   Create a `.env` file in the project root with the following variables:
   ```
   DB_USERNAME=your_mysql_username
   DB_PASSWORD=your_mysql_password
   DB_URL=jdbc:mysql://localhost:3306/stock_trading?createDatabaseIfNotExist=true
   ```

3. Build the project:
   ```
   mvn clean install
   ```

4. Run the application:
   ```
   mvn spring-boot:run
   ```

## Testing with grpcurl

[grpcurl](https://github.com/fullstorydev/grpcurl) is a command-line tool that lets you interact with gRPC servers.

### Installation

Follow the installation instructions in the [grpcurl GitHub repository](https://github.com/fullstorydev/grpcurl#installation).

### Usage Examples

1. List all available services:
   ```
   grpcurl -plaintext localhost:9090 list
   ```

2. Get service details:
   ```
   grpcurl -plaintext localhost:9090 list com.nadeex.stocktrading.StockTradingService
   ```

3. Get stock information:
   ```
   grpcurl -plaintext -d '{"stock_symbol": "AMZN"}' localhost:9090 com.nadeex.stocktrading.StockTradingService/getStockPrice
   ```

4. Stream stock orders:
   ```
   grpcurl -d @ -plaintext -import-path "C:\path\to\stock-trading-server\src\main\proto" -proto stock_trading.proto localhost:9090 com.nadeex.stocktrading.StockTradingService/bulkStockOrder < bulk_orders.json
   ```

## Configuration

The application is configured via `application.properties` file and environment variables. Key settings include:

- Database connection details (from `.env`)
- JPA/Hibernate settings
- Flyway migration configuration

## Security

- API keys and sensitive data are stored in environment variables
- `.env` files are excluded from version control
- Make sure to use proper authentication in production environments

## Note

This is a development setup. For production, consider using a secrets manager and proper deployment configurations.
