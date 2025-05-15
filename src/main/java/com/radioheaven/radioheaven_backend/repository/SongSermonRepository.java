package com.radioheaven.radioheaven_backend.repository;

import com.radioheaven.radioheaven_backend.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongSermonRepository extends JpaRepository<Song, Long> {
    // Custom query methods can be defined here if needed
}
