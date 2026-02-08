package com.myanime.domain.port.output;

import com.myanime.domain.models.AnimeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnimeRepository {
    List<AnimeModel> findByMinIdAndLimit(String minId, Integer limit);
    Page<AnimeModel> search(String name, Pageable pageable);
}
