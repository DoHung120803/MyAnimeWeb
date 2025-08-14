package com.myanime.domain.port.output;

import com.myanime.infrastructure.models.AnimeModel;

import java.util.List;

public interface AnimeRepository {
    List<AnimeModel> findByMinIdAndLimit(String minId, Integer limit);
}
