-- 기존 데이터 삭제 (테스트용)
-- 주의: 애플리케이션을 먼저 실행하여 테이블이 생성된 후 실행해야 합니다
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM coupon;
DELETE FROM coupon_event;
DELETE FROM member;
SET FOREIGN_KEY_CHECKS = 1;

-- 사용자 데이터 (부하테스트용)
INSERT INTO member (id, email, name) VALUES
(1, 'user1@test.com', '사용자1'),
(2, 'user2@test.com', '사용자2'),
(3, 'user3@test.com', '사용자3'),
(4, 'user4@test.com', '사용자4'),
(5, 'user5@test.com', '사용자5'),
(6, 'user6@test.com', '사용자6'),
(7, 'user7@test.com', '사용자7'),
(8, 'user8@test.com', '사용자8'),
(9, 'user9@test.com', '사용자9'),
(10, 'user10@test.com', '사용자10');

-- 부하테스트용 사용자 데이터 추가 (최대 20000명까지)
-- 필요시 스크립트로 생성

-- 쿠폰 이벤트 데이터
-- 시나리오 1: 순간 트래픽 테스트용 (1000개 재고)
INSERT INTO coupon_event (id, event_name, total_stock, remaining_stock, start_at, end_at, created_at) VALUES
(1, '순간 트래픽 테스트 이벤트', 1000, 1000, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), NOW());

-- 시나리오 3: 동시성 테스트용 (1개 재고)
INSERT INTO coupon_event (id, event_name, total_stock, remaining_stock, start_at, end_at, created_at) VALUES
(2, '동시성 테스트 이벤트', 1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), NOW());

-- 시나리오 4: 쿠폰 소진 후 테스트용 (0개 재고)
INSERT INTO coupon_event (id, event_name, total_stock, remaining_stock, start_at, end_at, created_at) VALUES
(3, '소진 후 테스트 이벤트', 0, 0, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), NOW());

-- 시나리오 5: 사용자별 중복 요청 테스트용 (1개 재고)
INSERT INTO coupon_event (id, event_name, total_stock, remaining_stock, start_at, end_at, created_at) VALUES
(4, '중복 요청 테스트 이벤트', 1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), NOW());

-- 시나리오 2: 점진적 부하 테스트용 (5000개 재고)
INSERT INTO coupon_event (id, event_name, total_stock, remaining_stock, start_at, end_at, created_at) VALUES
(5, '점진적 부하 테스트 이벤트', 5000, 5000, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), NOW());
