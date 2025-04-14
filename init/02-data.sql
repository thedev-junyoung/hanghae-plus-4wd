-- Products
INSERT INTO product (id, name, brand, price, release_date, image_url, description, created_at, updated_at)
VALUES
    (1, 'New Balance 993', 'New Balance', 199000, '2025-04-14', 'http://example.com/nb993.jpg', '미국산 프리미엄 쿠셔닝', NOW(), NOW()),
    (2, 'ASICS GEL-Kayano 14', 'ASICS', 169000, '2025-04-14', 'http://example.com/gelkayano14.jpg', '복각 러닝 슈즈의 정석', NOW(), NOW()),
    (3, 'New Balance 530', 'New Balance', 129000, '2025-04-14', 'http://example.com/nb530.jpg', '캐주얼한 데일리 슈즈', NOW(), NOW()),
    (4, 'Nike Daybreak', 'Nike', 109000, '2025-04-14', 'http://example.com/daybreak.jpg', '빈티지 감성 러닝화', NOW(), NOW()),
    (5, 'Nike Air Force 1', 'Nike', 139000, '2025-04-14', 'http://example.com/airforce1.jpg', '클래식 로우탑', NOW(), NOW()),
    (6, 'Autry Medalist', 'Autry', 185000, '2025-04-14', 'http://example.com/autry.jpg', '빈티지 미국 감성 스니커즈', NOW(), NOW());

-- Product Stocks
INSERT INTO product_stock (id, product_id, size, stock_quantity, updated_at)
VALUES
    (1, 1, 270, 50, NOW()),
    (2, 1, 280, 30, NOW()),
    (3, 2, 265, 40, NOW()),
    (4, 2, 275, 60, NOW()),
    (5, 3, 260, 70, NOW()),
    (6, 3, 270, 90, NOW()),
    (7, 4, 270, 55, NOW()),
    (8, 4, 280, 45, NOW()),
    (9, 5, 265, 80, NOW()),
    (10, 5, 275, 60, NOW()),
    (11, 6, 270, 35, NOW()),
    (12, 6, 280, 25, NOW());

-- Balance
INSERT INTO balance (id, user_id, amount, created_at, updated_at)
VALUES
    (1, 100, 500000, NOW(), NOW()),
    (2, 101, 300000, NOW(), NOW());

-- Coupon
INSERT INTO coupon (id, code, type, discount_rate, total_quantity, remaining_quantity, valid_from, valid_until)
VALUES
    (1, 'WELCOME10', 'PERCENTAGE', 10, 100, 98, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY)),
    (2, 'FLAT5000', 'FIXED', 5000, 50, 49, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY));


-- Coupon Issue
INSERT INTO coupon_issue (id, user_id, coupon_id, issued_at, is_used)
VALUES
    (1, 100, 1, NOW(), false),
    (2, 100, 2, NOW(), false);

-- Orders
INSERT INTO orders (id, user_id, total_amount, status, created_at)
VALUES
    ('order-1', 100, 398000, 'CONFIRMED', NOW());

-- Order Item
INSERT INTO order_item (id, product_id, quantity, size, price, order_id)
VALUES
    (1, 1, 1, 270, 199000, 'order-1'),
    (2, 2, 1, 275, 169000, 'order-1');

-- Payment
INSERT INTO payment (id, order_id, amount, status, method, created_at)
VALUES
    ('pay-1', 'order-1', 398000, 'SUCCESS', 'CARD', NOW());

-- Order Event
INSERT INTO order_event (id, aggregate_type, event_type, payload, status, created_at)
VALUES
    (UUID_TO_BIN(UUID()), 'ORDER', 'PAYMENT_COMPLETED', '{"orderId":"order-1"}', 'PENDING', NOW());

-- Product Statistics
INSERT INTO product_statistics (product_id, stat_date, sales_count, sales_amount)
VALUES
    (1, CURRENT_DATE, 1, 199000),
    (2, CURRENT_DATE, 1, 169000);
