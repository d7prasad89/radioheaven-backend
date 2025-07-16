package com.radioheaven.radioheaven_backend.song;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @RequestMapping("/all")
    ResponseEntity<List<Song>> getSongs() {
        List<Song> songList = songService.getSongs();
        if(songList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(songList);
    }

    @PostMapping("/add")
    void postSong(@RequestBody Song song) {
        // Implement the logic to add a new song
        // For example, you can save the song to the database
         songService.saveSong(song);
    }
}
