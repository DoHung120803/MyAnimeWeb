package com.myanime.infrastructure.adapters;

import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.models.OAuthAccountModel;
import com.myanime.domain.port.output.OAuthAccountRepository;
import com.myanime.infrastructure.entities.OAuthAccount;
import com.myanime.infrastructure.jparepos.OAuthAccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class OAuth2AccountAdapter implements OAuthAccountRepository {

    private final OAuthAccountJpaRepository oAuthAccountJpaRepository;

    @Override
    public OAuthAccountModel save(OAuthAccountModel oAuthAccountModel) {
        if (oAuthAccountModel == null) return null;

        OAuthAccount oAuthAccount = ModelMapperUtil.mapper(oAuthAccountModel, OAuthAccount.class);

        return ModelMapperUtil.mapper(oAuthAccountJpaRepository.save(oAuthAccount), OAuthAccountModel.class);

    }

    @Override
    public OAuthAccountModel findById(String provider, String providerUserId) {
        if (!StringUtils.hasText(provider) || !StringUtils.hasText(providerUserId)) return null;

        return oAuthAccountJpaRepository.findByIdProviderAndIdProviderUserId(provider, providerUserId)
                .map(oAuthAccount -> ModelMapperUtil.mapper(oAuthAccount, OAuthAccountModel.class))
                .orElse(null);
    }
}
