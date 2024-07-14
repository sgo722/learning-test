package sample.cafekiosk.spring.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {

    INIT("주문시작"),
    CANCELED("주문취소"),
    PAYMENT_COMPLETE("결제완료"),
    PAYMENT_CANCELED("결제취소");



    private final String text;
}
