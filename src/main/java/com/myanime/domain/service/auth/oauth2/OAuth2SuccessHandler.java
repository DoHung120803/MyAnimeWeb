package com.myanime.domain.service.auth.oauth2;

import com.myanime.common.constants.GlobalConstant;
import com.myanime.common.utils.StringUtil;
import com.myanime.domain.models.OAuthAccountModel;
import com.myanime.domain.models.UserModel;
import com.myanime.domain.port.output.OAuthAccountRepository;
import com.myanime.domain.port.output.UserRepository;
import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthAccountRepository oAuthAccountRepository;
    private final UserRepository userRepository;
    private final OAuth2AuthorizationCodeService oAuth2AuthorizationCodeService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {

            CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            String provider = customOAuth2User.getProvider();
            String providerUserId = customOAuth2User.getProviderUserId();

            OAuthAccountModel oAuthAccount = oAuthAccountRepository.findById(provider, providerUserId);

            UserModel user;

            if (oAuthAccount == null) {
                user = createNewOAuthAccount(customOAuth2User);
            } else {
                user = userRepository.findById(oAuthAccount.getUserId()).orElse(null);
            }

            String code = oAuth2AuthorizationCodeService.generateCode(user.getUsername());

            String redirectUrl = frontendUrl + "/oauth2/callback?code=" + code;

            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            log.error(">>> Failed to onAuthenticationSuccess: {}", e.getMessage(), e);
            Sentry.captureException(e);
            throw new RuntimeException("Failed to generate authorization code", e);
        }
    }

    private UserModel createNewOAuthAccount(CustomOAuth2User customOAuth2User) {
        String email = customOAuth2User.getEmail();

        // Nếu email không tồn tại trong hệ thống, tạo một người dùng mới, nếu tồn tại thì lấy thông tin người dùng từ hệ thống
        UserModel userModel = userRepository.findByEmail(email).orElseGet(() -> {
            UserModel newUser = new UserModel();
            newUser.setEmail(email);
            newUser.setDisplayName(customOAuth2User.getName());
            newUser.setFirstName(customOAuth2User.getGivenName());
            newUser.setLastName(customOAuth2User.getFamilyName());
            newUser.setAvtUrl(customOAuth2User.getPicture());
            newUser.setCreatedAt(LocalDateTime.now(ZoneId.of(GlobalConstant.TimeZone.ASIA_HO_CHI_MINH)));
            newUser.setUpdatedAt(LocalDateTime.now(ZoneId.of(GlobalConstant.TimeZone.ASIA_HO_CHI_MINH)));

            UUID id = UUID.randomUUID();
            newUser.setId(id.toString());

            // trường hợp tạo user oauth2 mới thì username sẽ được tạo ra từ id của user
            newUser.setUsername(StringUtil.removeSpaces(newUser.getDisplayName()) + "_" + StringUtil.encodeBase62(id));

            return userRepository.save(newUser);
        });

        OAuthAccountModel oAuthAccount = new OAuthAccountModel();

        OAuthAccountModel.OAuthAccountId id = new OAuthAccountModel.OAuthAccountId();
        id.setProvider(customOAuth2User.getProvider());
        id.setProviderUserId(customOAuth2User.getProviderUserId());

        oAuthAccount.setId(id);
        oAuthAccount.setUserId(userModel.getId());
        oAuthAccount.setCreatedAt(LocalDateTime.now(ZoneId.of(GlobalConstant.TimeZone.ASIA_HO_CHI_MINH)));
        oAuthAccount.setUpdatedAt(LocalDateTime.now(ZoneId.of(GlobalConstant.TimeZone.ASIA_HO_CHI_MINH)));

        oAuthAccountRepository.save(oAuthAccount);

        return userModel;

    }
}
