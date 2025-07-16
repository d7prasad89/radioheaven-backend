package com.radioheaven.radioheaven_backend.song;

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

    public void saveSong(Song song) {
        songRepository.save(song);
    }
}
