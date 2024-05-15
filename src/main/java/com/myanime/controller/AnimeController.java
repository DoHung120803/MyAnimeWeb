package com.myanime.controller;

import com.myanime.entity.Anime;
import com.myanime.model.dto.request.AnimeCreationRequest;
import com.myanime.model.dto.request.AnimeUpdateRequest;
import com.myanime.service.AnimeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animes")
public class AnimeController {
    @Autowired
    private AnimeServiceInterface animeService;

    // add anime into db
    @PostMapping()
    public Anime createAnime(@RequestBody AnimeCreationRequest request) {
        return animeService.createAnime(request);
    }

    // get all anime
    @GetMapping()
    public List<Anime> getAnimes() {
        return animeService.getAnimes();
    }

    @GetMapping("/{id}")
    public Anime getAnime(@PathVariable("id") String id) {
        return animeService.getAnime(id);
    }

    @PutMapping("/{id}")
    public Anime updateAnime(@PathVariable String id,@RequestBody AnimeUpdateRequest request) {
        return animeService.updateAnime(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteAnime(@PathVariable String id) {
        animeService.deleteAnime(id);
    }
}
