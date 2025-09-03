package com.radioheaven.radioheaven_backend.song;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongDTO {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String artist;
    @NotBlank
    private String album;
    @NotNull
    private Boolean isFavorite;
    @NotNull
    private Integer lengthInSeconds; // in seconds
}