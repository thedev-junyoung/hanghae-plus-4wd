package kr.hhplus.be.server.application.order;

public interface OrderUseCase {

    /**
     * 주문 생성 요청을 처리
     */
    OrderResult createOrder(CreateOrderCommand command);
}
