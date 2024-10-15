package com.coopang.product.api_commnuication;

public interface MessageConsumer {
    // 메시지를 소비하는 메서드
    void consume(String message);
}
