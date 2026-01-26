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
    ResponseEntity<List<SongDTO>> getSongs() {
        var songList = songService.getSongs(0, 10).getContent();
        if(songList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<SongDTO> dtoList = songList.stream()
                .map(song -> new SongDTO(
                        song.getId(),
                        song.getTitle(),
                        song.getArtist(),
                        song.getAlbum(),
                        song.isFavorite(),
                        song.getLengthInSeconds(),
                        song.getCoverImageUrl(),
                        song.getAudioUrl()
                ))
                .toList();

        return ResponseEntity.ok(dtoList);
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
        song.setFavorite(songDTO.getIsFavorite());
        song.setAudioUrl(songDTO.getFileUrl());
        song.setCoverImageUrl(songDTO.getCoverImageURL());

        // Save the song using the songService
         songService.saveSong(song);
    }

    @PutMapping("/update/{id}")
    SongDTO updateSong(@RequestBody @Valid SongDTO songDTO, @PathVariable Long id) {
        // Implement the logic to update an existing song
        if (songDTO.getTitle() == null || songDTO.getTitle().isEmpty()
                || id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid song data");
        }
        Song song = songService.getSongById(id);
        if (song == null) {
            throw new IllegalArgumentException("Song with id " + id + " does not exist");
        }
        song.setTitle(songDTO.getTitle());
        song.setArtist(songDTO.getArtist());
        song.setAlbum(songDTO.getAlbum());
        song.setArtist(songDTO.getArtist());
        song.setLengthInSeconds(songDTO.getLengthInSeconds());
        song.setFavorite(songDTO.getIsFavorite());
        song.setAudioUrl(songDTO.getFileUrl());
        song.setCoverImageUrl(songDTO.getCoverImageURL());

        // Save the updated song using the songService
        Song updatedSong = songService.saveSong(song);
        if (updatedSong!= null) {
            return new SongDTO(
                    updatedSong.getId(),
                    updatedSong.getTitle(),
                    updatedSong.getArtist(),
                    updatedSong.getAlbum(),
                    updatedSong.isFavorite(),
                    updatedSong.getLengthInSeconds(),
                    "",
                    ""
            );
        } else {
            throw new IllegalStateException("Failed to update the song");
        }
    }
}
