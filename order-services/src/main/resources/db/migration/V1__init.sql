CREATE TABLE IF NOT EXISTS t_orders (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `order_number` varchar(255) DEFAULT NULL,
    `sku_code` varchar(255) DEFAULT NULL,
    `price` decimal(19, 2) DEFAULT NULL,
    `quantity` int(11) DEFAULT NULL,
    PRIMARY KEY (id)
);