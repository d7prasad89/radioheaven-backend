package com.radioheaven.radioheaven_backend.song;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongDTO {
    @NotNull
    private String title;
    @NotNull
    private String artist;
    @NotNull
    private String album;
    @NotNull
    private boolean isFavorite;
    @NotNull
    private int lengthInSeconds; // in seconds
}
