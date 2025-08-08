package com.myanime.infrastructure.adapters;

import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.port.output.AnimeRepository;
import com.myanime.infrastructure.jparepos.jpa.AnimeJpaRepository;
import com.myanime.infrastructure.models.AnimeModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnimeAdapter implements AnimeRepository {

    private final AnimeJpaRepository animeJpaRepository;

    @Override
    public List<AnimeModel> findByMinIdAndLimit(String minId, Integer limit) {
        return ModelMapperUtil.mapList(
                animeJpaRepository.findByMinIdAndLimit(minId, limit),
                AnimeModel.class
        );
    }
}
