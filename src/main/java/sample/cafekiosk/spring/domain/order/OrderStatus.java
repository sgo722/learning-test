package sample.cafekiosk.spring.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {

    INIT("주문생성"),
    CANCELED("주문취소"),
    PAYMENT_COMPLETE("결제왼료"),
    PAYMENT_FAILED("결제취소"),
    RECEIVED("주문접수"),
    COMPLETE("처리완료");

    private final String text;
}
