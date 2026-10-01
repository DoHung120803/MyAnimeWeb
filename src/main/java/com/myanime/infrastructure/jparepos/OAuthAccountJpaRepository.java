package com.myanime.infrastructure.jparepos;

import com.myanime.infrastructure.entities.OAuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthAccountJpaRepository extends JpaRepository<OAuthAccount, OAuthAccount.OAuthAccountId> {
    Optional<OAuthAccount> findByIdProviderAndIdProviderUserId(String provider, String providerUserId);
}
