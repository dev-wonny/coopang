# 메시지 브로커 전략 패턴 문서 (Kafka/RabbitMQ)

> 제출 형식: Notion/PDF에 붙여넣기 가능한 마크다운

## 1) 개발 소스 코드 (Java, 500줄 미만, 주석 포함)

### 1-1. 전략 인터페이스 (Strategy)
```java
// 전략 패턴의 Strategy 역할 인터페이스
// 브로커(Kafka/RabbitMQ 등)를 교체해도 sendMessage 시그니처를 유지
package com.coopang.apicommunication.kafka.producer;

public interface MessageProducer {
    // 메시지 전송 전략의 공통 인터페이스
    void sendMessage(String topic, String message);
}
```

### 1-2. Kafka 전략 구현체 (ConcreteStrategy)
```java
// Kafka 전용 ConcreteStrategy 구현체
// MessageProducer 인터페이스를 구현하여 Kafka를 전략으로 제공
package com.coopang.apicommunication.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j(topic = "KafkaMessageService")
@Service
@ConditionalOnProperty(name = "message.broker", havingValue = "kafka", matchIfMissing = true)
public class KafkaMessageProducerService implements MessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMessageProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(String topic, String message) {
        // Kafka 브로커로 메시지 전송
        kafkaTemplate.send(topic, message);
    }
}
```

### 1-3. RabbitMQ 전략 구현체 (ConcreteStrategy)
```java
// RabbitMQ 전용 ConcreteStrategy 구현체
// 동일한 MessageProducer 인터페이스를 구현하여 브로커를 교체 가능하게 함
package com.coopang.apicommunication.rabbit.producer;

import com.coopang.apicommunication.kafka.producer.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j(topic = "RabbitMessageService")
@Service
@ConditionalOnProperty(name = "message.broker", havingValue = "rabbit")
public class RabbitMessageProducerService implements MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMessageProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendMessage(String topic, String message) {
        // RabbitMQ 기본 exchange에 routingKey(topic)로 메시지 발행
        rabbitTemplate.convertAndSend("", topic, message);
    }
}
```

> 위 3개 코드 블록 합산은 500줄 미만입니다.

---

## 2) 코드 설명

### 전략 패턴 적용 구조
- **`MessageProducer`**: 전략 패턴의 **Strategy 인터페이스**
- **`KafkaMessageProducerService`**: Kafka를 사용하는 **ConcreteStrategy**
- **`RabbitMessageProducerService`**: RabbitMQ를 사용하는 **ConcreteStrategy**

### 선택 방식
- Spring의 `@ConditionalOnProperty`를 사용해 런타임 설정으로 전략 선택
- 예시 설정
  ```yaml
  message:
    broker: kafka   # 또는 rabbit
    retry:
      max-attempts: 3
      backoff-ms: 200
  ```

---

## 3) 화면 개발 방식 및 구현 과정 (상세 기술)

본 모듈은 **메시지 브로커 인프라 레이어**로, **화면(UI) 개발 요소가 존재하지 않습니다.**
따라서 화면 설계/구현 과정은 **해당 없음(N/A)**으로 기록합니다.

대신, 백엔드 서비스 흐름 관점의 구현 과정을 기술합니다.

### 구현 과정
1. **전략 인터페이스 정의**: `MessageProducer`로 메시지 전송 방식의 공통 추상화를 설계
2. **Kafka 구현체 작성**: `KafkaMessageProducerService`에서 KafkaTemplate로 전송
3. **RabbitMQ 구현체 작성**: `RabbitMessageProducerService`에서 RabbitTemplate로 전송
4. **런타임 선택 로직 적용**: `message.broker` 값에 따라 전략이 선택되도록 구성

---

## 4) 쿼리 구조 / DB 설계 구조

- 본 모듈은 **메시지 브로커 전송 로직**으로 구성되어 있으며
- **DB 접근/쿼리/스키마 설계와 직접적인 연관이 없습니다.**

따라서 **DB 설계 항목은 없음**으로 기술합니다.

---

## 5) 제출 가이드 (Notion/PDF 권장 구성)

1. **Notion**: 본 문서를 그대로 복사하여 Notion 페이지에 붙여넣기
2. **PDF**: Notion → `Export → PDF` 또는 Markdown → PDF 변환

---

## 6) 개선 제안 (부족한 코드 보완 방향)

- **실행 환경 선택의 명확화**: `message.broker` 설정을 서비스별 `application.yml`에 명시
- **실패 처리 개선**: Kafka/Rabbit 전송 결과에 대한 실패 로깅 및 재시도 정책 추가
- **소비자(Consumer) 전략화 확장**: 메시지 소비 측도 동일한 전략 패턴으로 확장 가능

---

## 7) 선택 기준 (본인 역량 강조 포인트)

- **전략 패턴 기반 설계 능력**
- **메시징 인프라 추상화 및 교체 가능 구조**
- **Spring Boot 조건부 빈 등록을 통한 런타임 전략 선택**