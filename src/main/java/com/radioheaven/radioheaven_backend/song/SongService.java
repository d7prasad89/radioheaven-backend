package com.radioheaven.radioheaven_backend.song;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SongService {
    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Page<Song> getSongs(int page, int size) {
        return songRepository.findAll(Pageable.ofSize(page));
    }

    public Song saveSong(Song song) {
        songRepository.save(song);
        return song;
    }

    public boolean songExistsByTitle(String title) {
        return songRepository.existsByTitle(title);
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id).orElse(null);
    }
}
