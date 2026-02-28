package com.myanime.domain.service.jobs;

import com.myanime.common.constants.GlobalConstant;
import com.myanime.domain.models.AnimeModel;
import com.myanime.domain.port.input.job.SyncUC;
import com.myanime.domain.port.output.AnimeRepository;
import com.myanime.infrastructure.elasticsearch.ESDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("syncAnimeService")
@Slf4j
public class SyncAnimeService extends AbstractSyncService<AnimeModel> implements SyncUC {

    public SyncAnimeService(AnimeRepository animeRepository, ESDocument esDocument) {
        super(animeRepository, esDocument);
    }

    @Override
    protected String getIndexName() {
        return GlobalConstant.ESIndex.ANIMES;
    }

    @Override
    protected String getId(AnimeModel model) {
        return model.getId();
    }

    @Override
    protected Map<String, Object> toIndexMap(AnimeModel anime) {
        Map<String, Object> mapData = new HashMap<>();

        mapData.put("id", anime.getId());
        mapData.put("name", anime.getName());
        mapData.put("description", anime.getDescription());
        mapData.put("rate", anime.getRate());
        mapData.put("views", anime.getViews());
        mapData.put("iframe", anime.getIframe());
        mapData.put("thumbnailUrl", anime.getThumbnailUrl());

        return mapData;
    }

    @Override
    protected String getEntityName() {
        return "animes";
    }
}
