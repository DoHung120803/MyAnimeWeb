package com.myanime.domain.port.output;

import com.myanime.domain.models.AnimeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnimeRepository extends SyncableRepository<AnimeModel> {
    Page<AnimeModel> search(String name, Pageable pageable);
}
