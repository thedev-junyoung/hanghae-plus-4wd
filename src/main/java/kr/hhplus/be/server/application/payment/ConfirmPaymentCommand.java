package kr.hhplus.be.server.application.payment;

public record ConfirmPaymentCommand(
        String pgTransactionId
) {
}
