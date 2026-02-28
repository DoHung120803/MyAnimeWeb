package com.myanime.infrastructure.adapters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.myanime.common.constants.GlobalConstant;
import com.myanime.common.utils.JsonUtil;
import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.models.UserModel;
import com.myanime.domain.port.output.UserRepository;
import com.myanime.infrastructure.jparepos.UserJpaRepository;
import com.myanime.infrastructure.projections.ConversationUserInfoProjection;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final RestHighLevelClient restHighLevelClient;

    @Override
    public long countByIdIn(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) return 0;

        return userJpaRepository.countByIdIn(ids);
    }

    @Override
    public boolean existsById(String id) {
        if (!StringUtils.hasText(id)) return false;

        return userJpaRepository.existsById(id);
    }

    @Override
    public Optional<UserModel> findById(String id) {
        if (!StringUtils.hasText(id)) return Optional.empty();

        return userJpaRepository.findById(id)
                .map(user -> ModelMapperUtil.mapper(user, UserModel.class));
    }

    @Override
    public List<UserModel> findAllByIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) return List.of();

        return ModelMapperUtil.mapList(userJpaRepository.findAllById(ids), UserModel.class);
    }

    @Override
    public List<UserModel> getConversationUserInfo(List<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return List.of();
        }

        List<ConversationUserInfoProjection> projections = userJpaRepository.getConversationUserInfoByIds(userIds);

        return ModelMapperUtil.mapList(projections, UserModel.class);
    }

    @Override
    public List<UserModel> findByMinIdAndLimit(String minId, Integer limit) {
        return ModelMapperUtil.mapList(
                userJpaRepository.findByMinIdAndLimit(minId, limit),
                UserModel.class
        );
    }

    @Override
    public Page<UserModel> search(String keyword, Pageable pageable) {
        if (keyword == null) return new PageImpl<>(List.of());

        Page<UserModel> result;

        try {
            result = searchByES(keyword, pageable);
        } catch (Exception e) {
            log.error(">>> Error searching user by elasticsearch: {}", keyword, e);
            result = ModelMapperUtil.mapPage(userJpaRepository.searchUser(keyword, pageable), UserModel.class);
        }

        return result;
    }

    private Page<UserModel> searchByES(String keyword, Pageable pageable) throws IOException {

        if (keyword == null || keyword.trim().isEmpty()) {
            return Page.empty(pageable);
        }

        SearchRequest searchRequest = new SearchRequest(GlobalConstant.ESIndex.USERS);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(
                        QueryBuilders.multiMatchQuery(keyword)
                                .field("username", 3.0f)
                                .field("fullName", 2.0f)
                                .fuzziness(Fuzziness.AUTO)
                )
                .from((int) pageable.getOffset())
                .size(pageable.getPageSize())
                .fetchSource(true)
                .trackScores(true);   // nên bật để ES ranking

        searchRequest.source(sourceBuilder);

        SearchResponse response =
                restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        List<UserModel> content = new ArrayList<>();

        for (SearchHit hit : response.getHits().getHits()) {
            UserModel user = JsonUtil.jsonToObject(
                    hit.getSourceAsString(),
                    new TypeReference<>() {
                    }
            );
            content.add(user);
        }

        long totalHits = Objects.requireNonNull(response.getHits().getTotalHits()).value;

        return new PageImpl<>(content, pageable, totalHits);
    }
}

