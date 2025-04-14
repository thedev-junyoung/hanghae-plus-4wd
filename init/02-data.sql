-- Products
INSERT INTO product (id, name, brand, price, release_date, image_url, description, created_at, updated_at)
VALUES
    (1, 'Nike Air Max', 'Nike', 120000, '2025-04-14', 'http://example.com/nike.jpg', '편안한 쿠셔닝', '2025-04-14 13:09:24', '2025-04-14 13:09:24'),
    (2, 'Adidas Ultra Boost', 'Adidas', 150000, '2025-04-14', 'http://example.com/adidas.jpg', '달리기용 최고급 스니커즈', '2025-04-14 13:09:24', '2025-04-14 13:09:24');

-- Product Stocks
INSERT INTO product_stock (id, product_id, size, stock_quantity, updated_at)
VALUES
    (1, 1, 270, 100, '2025-04-14 13:09:24'),
    (2, 1, 280, 50, '2025-04-14 13:09:24'),
    (3, 2, 270, 120, '2025-04-14 13:09:24'),
    (4, 2, 280, 80, '2025-04-14 13:09:24');

-- Balance
INSERT INTO balance (id, user_id, amount, created_at, updated_at)
VALUES
    (1, 100, 500000, '2025-04-14 13:09:24', '2025-04-14 13:09:24');

-- Coupon
INSERT INTO coupon (id, code, type, discount_rate, total_quantity, remaining_quantity, valid_from, valid_until)
VALUES
    (1, 'WELCOME10', 'PERCENTAGE', 10, 100, 100, '2025-04-14 13:09:24', '2025-05-14 13:09:24'),
    (2, 'FLAT5000', 'FIXED', 5000, 50, 50, '2025-04-14 13:09:24', '2025-05-14 13:09:24');

-- Coupon Issue
INSERT INTO coupon_issue (id, user_id, coupon_id, issued_at, is_used)
VALUES
    (1, 100, 1, '2025-04-14 13:09:24', false),
    (2, 100, 2, '2025-04-14 13:09:24', false);

-- Orders
INSERT INTO orders (id, user_id, total_amount, status, created_at)
VALUES
    ('order-1', 100, 240000, 'CREATED', '2025-04-14 13:09:24');

-- Order Item
INSERT INTO order_item (id, product_id, quantity, size, price, order_id)
VALUES
    (1, 1, 2, 270, 120000, 'order-1');

-- Payment
INSERT INTO payment (id, order_id, amount, status, method, created_at)
VALUES
    ('pay-1', 'order-1', 240000, 'SUCCESS', 'CARD', '2025-04-14 13:09:24');

-- Order Event
INSERT INTO order_event (id, aggregate_type, event_type, payload, status, created_at)
VALUES
    (UUID_TO_BIN(UUID()), 'ORDER', 'PAYMENT_COMPLETED', '{"orderId":"order-1"}', 'PENDING', '2025-04-14 13:09:24');

-- Product Statistics
INSERT INTO product_statistics (product_id, stat_date, sales_count, sales_amount)
VALUES
    (1, '2025-04-14', 2, 240000);
