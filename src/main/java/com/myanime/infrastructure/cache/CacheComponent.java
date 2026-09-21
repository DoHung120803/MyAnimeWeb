package com.myanime.infrastructure.cache;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Setter
public class CacheComponent<K, V> {
    private final RedisTemplate<K, V> redisTemplate;
    private String cacheHasKey;
    private long timeout;
    private TimeUnit timeUnit;

    public CacheComponent(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public V get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Error getting value from cache for key {}: {}", key, e.getMessage());
            return null;
        }
    }

    public void set(K key, V value, long timeout, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        } catch (Exception e) {
            log.error("Error setting value in cache for key {}: {}", key, e.getMessage());
        }
    }
}
