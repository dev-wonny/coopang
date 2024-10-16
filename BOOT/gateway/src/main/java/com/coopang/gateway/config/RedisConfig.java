package com.coopang.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 키 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        // 값 직렬화
        template.setValueSerializer(new StringRedisSerializer());
        // 해시 키 직렬화
        template.setHashKeySerializer(new StringRedisSerializer());
        // 해시 값 직렬화
        template.setHashValueSerializer(new StringRedisSerializer());

        template.setEnableTransactionSupport(true);  // 트랜잭션 활성화
        return template;
    }
}
