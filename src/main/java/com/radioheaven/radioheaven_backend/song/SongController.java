package com.radioheaven.radioheaven_backend.song;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "*")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @RequestMapping("/all")
    ResponseEntity<List<Song>> getSongs() {
        List<Song> songList = songService.getSongs(10, 10).getContent();
        if(songList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(songList);
    }

    @PostMapping("/add")
    void postSong(@RequestBody @Valid SongDTO songDTO) {
        // Implement the logic to add a new song
        // For example, you can save the song to the database
        if (songService.songExistsByTitle(songDTO.getTitle())) {
            throw new IllegalArgumentException("Song with the same title already exists");
        }

        Song song = new Song();
        song.setTitle(songDTO.getTitle());
        song.setArtist(songDTO.getArtist());
        song.setAlbum(songDTO.getAlbum());
        song.setArtist(songDTO.getArtist());
        song.setLengthInSeconds(songDTO.getLengthInSeconds());

        // Save the song using the songService
         songService.saveSong(song);
    }
}
