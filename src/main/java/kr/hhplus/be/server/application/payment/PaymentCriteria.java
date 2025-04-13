package kr.hhplus.be.server.application.payment;

public record PaymentCriteria (
        String orderId,
        long amount,
        String method
){
    public static PaymentCriteria of(RequestPaymentCommand command) {
        return new PaymentCriteria(
                command.orderId(),
                command.amount(),
                command.method()
        );
    }
}
