package com.myanime.domain.service.auth.oauth2;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomOAuth2User implements OAuth2User, OidcUser {
    private OAuth2User oauth2User;

    // dùng cho login bằng OIDC (google)
    private OidcUser oidcUser;

    private String provider;
    private String providerUserId;
    private String email;
    private String givenName;
    private String familyName;
    private String name;
    private String picture;

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }
}
