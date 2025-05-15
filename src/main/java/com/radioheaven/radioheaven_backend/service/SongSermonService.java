package com.radioheaven.radioheaven_backend.service;

import com.radioheaven.radioheaven_backend.model.Song;
import com.radioheaven.radioheaven_backend.repository.SongSermonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongSermonService {
    private final SongSermonRepository songSermonRepository;

    public SongSermonService(SongSermonRepository songSermonRepository) {
        this.songSermonRepository = songSermonRepository;
    }

    public List<Song> getSongs() {
        return songSermonRepository.findAll();
    }

    public void saveSong(Song song) {
        songSermonRepository.save(song);
    }
}
