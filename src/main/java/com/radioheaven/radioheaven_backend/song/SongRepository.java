package com.radioheaven.radioheaven_backend.song;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long>,
org.springframework.data.repository.PagingAndSortingRepository<Song, Long> {
    boolean existsByTitle(String title);
}
