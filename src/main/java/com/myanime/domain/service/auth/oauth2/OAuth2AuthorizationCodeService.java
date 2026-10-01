package com.myanime.domain.service.auth.oauth2;

import com.myanime.domain.exceptions.OAuth2Exception;
import com.myanime.infrastructure.cache.CacheComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OAuth2AuthorizationCodeService {
    private final CacheComponent<String, String> redisTemplate;

    private static final String PREFIX = "oauth2:authorization-code:";

    public String generateCode(String username) {

        String code = UUID.randomUUID().toString();
        String key = PREFIX + code;

        redisTemplate.set(key, username, 60, TimeUnit.SECONDS);

        return code;
    }

    public String consumeCode(String code) throws OAuth2Exception {

        String key = PREFIX + code;

        String userId = redisTemplate.getAndDelete(key);

        if (!StringUtils.hasText(userId)) {
            throw new OAuth2Exception("Có lỗi xảy ra. Vui lòng thử lại.");
        }

        return userId;
    }

}
