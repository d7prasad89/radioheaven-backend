package com.radioheaven.radioheaven_backend.controller;

import com.radioheaven.radioheaven_backend.model.Song;
import com.radioheaven.radioheaven_backend.repository.SongRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    SongRepository songRepository;

    public SongController(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @RequestMapping("/all")
    ResponseEntity<List<Song>> getSongs() {
        List<Song> songList = songRepository.findAll();
        if(songList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(songList);
    }
}
