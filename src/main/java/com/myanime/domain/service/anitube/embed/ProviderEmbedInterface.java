package com.myanime.domain.service.anitube.embed;

import com.myanime.application.rest.requests.anitube.AniTubeEmbedRequest;
import com.myanime.domain.dtos.anitube.AniTubeEmbeddedVideoDTO;
import io.sentry.Sentry;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface ProviderEmbedInterface {

    boolean supports(String provider);

    void validate(AniTubeEmbedRequest request);

    ProviderEmbedInterface execute();

    Object getData(AniTubeEmbedRequest request);

    default Object handleBatch(AniTubeEmbedRequest request, Supplier<Future<List<AniTubeEmbeddedVideoDTO>>> supplier, short batchSize) {
        List<Future<List<AniTubeEmbeddedVideoDTO>>> futures = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            futures.add(supplier.get());
        }

        return futures.stream().map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        Sentry.captureException(e);
                        return null;
                    }
                }).filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toMap(
                        AniTubeEmbeddedVideoDTO::getId,
                        Function.identity(),
                        (existing, duplicate) -> existing
                ))
                .values()
                .stream()
                .filter(dto -> StringUtils.hasText(dto.getId()) && StringUtils.hasText(dto.getDesc()))
                .toList();
    }
}
