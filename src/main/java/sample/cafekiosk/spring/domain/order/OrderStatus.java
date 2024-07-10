package sample.cafekiosk.spring.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {

    INIT("주문 생성"),
    CANCELED("주문 취소"),
    PAYMENT_COMPLETE("결제 완료"),
    PAYMENT_FAILED("결제 취소"),
    RECEIVED("결제 완료"),
    COMPLETED("결제 완료");



    private final String text;
}
