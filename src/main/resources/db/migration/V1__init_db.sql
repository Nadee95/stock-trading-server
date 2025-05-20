
CREATE TABLE stocks (
                        id INT AUTO_INCREMENT PRIMARY KEY ,
                        stock_symbol VARCHAR(10) UNIQUE NOT NULL ,
                        price DOUBLE PRECISION NOT NULL,
                        currency VARCHAR(3) NOT NULL DEFAULT 'USD',
                        last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);