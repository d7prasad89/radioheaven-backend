package com.radioheaven.radioheaven_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.radioheaven.radioheaven_backend.model.Song;

public interface SongRepository extends MongoRepository<Song, String> {
    boolean existsSongsByTitle(String title);
}
