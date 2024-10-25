package com.myanime.service.elasticsearch.anime;

import com.myanime.mapper.AnimeMapper;
import com.myanime.model.dto.response.AnimeResponse;
import com.myanime.repository.elasticsearch.AnimeESRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeESServiceImpl implements AnimeESService {
    private final AnimeESRepository animeESRepository;
    private final AnimeMapper animeMapper;

    @Override
    public List<AnimeResponse> searchByNameOrDesc(String term) {
        Pageable pageable = PageRequest.of(0, 20);
        return animeMapper.toAnimeResponseList(animeESRepository.searchByNameOrDesc(term, pageable));
    }
}
