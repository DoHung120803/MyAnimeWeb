package com.myanime.domain.port.output;

import com.myanime.domain.models.OAuthAccountModel;

public interface OAuthAccountRepository {
    OAuthAccountModel save(OAuthAccountModel oAuthAccountModel);
    OAuthAccountModel findById(String provider, String providerUserId);
}
