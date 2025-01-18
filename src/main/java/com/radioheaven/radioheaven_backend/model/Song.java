package com.radioheaven.radioheaven_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Song {

    @Id
    private String id;
    private String title;
    private String artist;
    private String fileName;
    private boolean isFavorited;
}
