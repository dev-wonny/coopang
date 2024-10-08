package com.coopang.product.infrastructure.configuration;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@EnableKafka
@Configuration
public class ConsumerApplicationKafkaConfig {

    //TODO : 추후 하드코딩 된 내용은 환경변수로 변경
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "product-sever-group");  // 그룹 아이디 설정
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
        DefaultErrorHandler errorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    /**
     * 지수 백오프 전략 생각
     * ExponentialBackOff backOff = new ExponentialBackOff(1000L, 2.0);
     *     backOff.setMaxInterval(30000L); // 최대 대기 시간 설정
     * @param template
     * @return
     */
    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> template) {
        // DeadLetterPublishingRecoverer를 사용하여 실패한 메시지를 DLT로 전송
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template);
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 0)); // 1초 대기, 재시도 횟수
    }
}
