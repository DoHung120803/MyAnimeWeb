package com.myanime.domain.service.anitube.embed;

import com.myanime.common.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderEmbedFactory {
    private final List<ProviderEmbedInterface> providerEmbedList;

    public ProviderEmbedInterface getProviderEmbed(String provider) throws BadRequestException {
        return providerEmbedList.stream()
                .filter(providerEmbed -> providerEmbed.supports(provider))
                .findFirst()
                .map(ProviderEmbedInterface::execute)
                .orElseThrow(() -> new BadRequestException("Provider không hợp lệ: " + provider));
    }
}
