package com.myanime.domain.service.auth.oauth2;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        OidcUser user = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        String providerUserId = user.getSubject();
        String email = user.getEmail();
        String name = user.getFullName();
        String givenName = user.getGivenName();
        String familyName = user.getFamilyName();
        String picture = user.getPicture();

        return CustomOAuth2User.builder()
                .oauth2User(user)
                .oidcUser(user)
                .provider(provider)
                .providerUserId(providerUserId)
                .email(email)
                .name(name)
                .givenName(givenName)
                .familyName(familyName)
                .picture(picture)
                .build(
        );
    }
}