package com.myanime.config.caches;

import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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
        return redisTemplate.opsForValue().get(key);
    }

    public void set(K key, V value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }
}
