package com.myanime.infrastructure.adapters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.myanime.common.utils.JsonUtil;
import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.port.output.AnimeRepository;
import com.myanime.infrastructure.entities.jpa.Anime;
import com.myanime.infrastructure.jparepos.jpa.AnimeJpaRepository;
import com.myanime.infrastructure.models.AnimeModel;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.myanime.common.constants.ElasticsearchConstant.ANIME_INDEX_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnimeAdapter implements AnimeRepository {

    private final AnimeJpaRepository animeJpaRepository;
    private final RestHighLevelClient restHighLevelClient;

    @Override
    public List<AnimeModel> findByMinIdAndLimit(String minId, Integer limit) {
        return ModelMapperUtil.mapList(
                animeJpaRepository.findByMinIdAndLimit(minId, limit),
                AnimeModel.class
        );
    }

    @Override
    public Page<AnimeModel> search(String name, Pageable pageable) {
        if (name == null || name.isEmpty()) {
            return new PageImpl<>(List.of(), Page.empty().getPageable(), 0);
        }

        Page<AnimeModel> page;

        try {
            page = searchByES(name, pageable);
        } catch (IOException e) {
            log.error(">>> Error searching anime by elasticsearch: {}", name, e);
            Sentry.captureException(e);

            page = searchByDB(name, pageable);
        }

        return page;

    }

    private Page<AnimeModel> searchByES(String name, Pageable pageable) throws IOException {
        SearchRequest searchRequest = new SearchRequest(ANIME_INDEX_NAME);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.matchQuery("name", name)
                        .fuzziness(Fuzziness.AUTO)
                )
                .from((int) pageable.getOffset())  // offset = page * size
                .size(pageable.getPageSize())
                .fetchSource(true)        // Chỉ lấy _source
                .trackScores(false);      // Không cần _score

        searchRequest.source(sourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        List<AnimeModel> content = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            AnimeModel anime = JsonUtil.jsonToObject(hit.getSourceAsString(), new TypeReference<AnimeModel>() {});
            content.add(anime);
        }

        long totalHits = response.getHits().getTotalHits().value;

        return new PageImpl<>(content, pageable, totalHits);
    }

    private Page<AnimeModel> searchByDB(String name, Pageable pageable) {
        Page<Anime> animePage = animeJpaRepository.findByNameContaining(name, pageable);

        return ModelMapperUtil.mapPage(
                animePage,
                AnimeModel.class
        );
    }
}
