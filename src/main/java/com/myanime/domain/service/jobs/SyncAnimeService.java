package com.myanime.domain.service.jobs;

import com.myanime.domain.port.input.job.SyncAnimeUC;
import com.myanime.domain.port.output.AnimeRepository;
import com.myanime.infrastructure.elasticsearch.Bulk;
import com.myanime.infrastructure.elasticsearch.ESDocument;
import com.myanime.domain.models.AnimeModel;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.myanime.common.constants.ElasticsearchConstant.ANIME_INDEX_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyncAnimeService implements SyncAnimeUC {

    private final AnimeRepository animeRepository;

    private static final Integer LIMIT = 200;
    private final ESDocument esDocument;

    @Override
    public void run() {
        String minId = null;

        Bulk bulkRequest =  new Bulk();

        while (true) {
            List<AnimeModel> listAnime = animeRepository.findByMinIdAndLimit(minId, LIMIT);

            if (listAnime.isEmpty()) {
                break; // No more data to process
            }

            log.info("Start syncing {} animes with minId: {}", listAnime.size(), minId);

            try {
                for (AnimeModel anime : listAnime) {
                    String id = anime.getId();

                    Map<String, Object> mapData = new HashMap<>();

                    mapData.put("id", id);
                    mapData.put("name", anime.getName());
                    mapData.put("description", anime.getDescription());
                    mapData.put("rate", anime.getRate());
                    mapData.put("views", anime.getViews());
                    mapData.put("iframe", anime.getIframe());
                    mapData.put("thumbnail_url", anime.getThumbnailUrl());
                    mapData.put("created_at", anime.getCreatedAt());
                    mapData.put("updated_at", anime.getUpdatedAt());

                    bulkRequest.addBulkRequestUpdate(
                            id,
                            ANIME_INDEX_NAME,
                            mapData
                    );
                }

                minId = listAnime.getLast().getId();
                esDocument.executeBulkRequest(bulkRequest.getBulkRequest());
                bulkRequest.clear();

            } catch (Exception e) {
                log.error(">>> Error syncing animes with minId: {}. Error: {}", minId, e.getMessage(), e);
                Sentry.captureException(e);
            }
        }

        log.info("Syncing animes completed successfully.");
    }
}
