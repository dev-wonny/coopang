package com.coopang.product.api_commnuication;

public interface MessageService {
    // 메시지를 받아서 도메인별로 처리하는 메서드
    void processMessage(String topic, String message);
}
