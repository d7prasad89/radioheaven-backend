package com.radioheaven.radioheaven_backend.service;

import com.radioheaven.radioheaven_backend.model.Song;
import com.radioheaven.radioheaven_backend.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {
    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> getSongs() {
        return songRepository.findAll();
    }
}
